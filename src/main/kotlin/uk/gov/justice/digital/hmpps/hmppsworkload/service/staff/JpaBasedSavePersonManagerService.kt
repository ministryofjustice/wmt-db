package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import javax.transaction.Transactional

@Service
class JpaBasedSavePersonManagerService(
  private val personManagerRepository: PersonManagerRepository,
  private val workloadCalculationService: WorkloadCalculationService,
  private val getPersonManager: GetPersonManager,
) : SavePersonManagerService {
  @Transactional
  override fun savePersonManager(
    teamCode: String,
    deliusStaff: DeliusStaff,
    loggedInUser: String,
    personSummary: PersonSummary,
    crn: String
  ): SaveResult<PersonManagerEntity> =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)?.let { personManager ->
      if (personManager.staffId == deliusStaff.staffIdentifier && personManager.teamCode == teamCode) {
        SaveResult(personManager, false)
      } else {
        val currentPersonManager = getPersonManager.findLatestByCrn(crn)
        createPersonManager(deliusStaff, teamCode, personSummary, loggedInUser, crn).also {
          personManager.isActive = false
          workloadCalculationService.calculate(currentPersonManager!!.staffCode, currentPersonManager.teamCode, currentPersonManager.staffGrade)
        }
      }
    } ?: createPersonManager(deliusStaff, teamCode, personSummary, loggedInUser, crn)

  private fun createPersonManager(
    deliusStaff: DeliusStaff,
    teamCode: String,
    personSummary: PersonSummary,
    loggedInUser: String,
    crn: String
  ): SaveResult<PersonManagerEntity> {
    val personManagerEntity = PersonManagerEntity(
      crn = crn,
      staffId = deliusStaff.staffIdentifier,
      staffCode = deliusStaff.staffCode,
      teamCode = teamCode,
      offenderName = "${personSummary.firstName} ${personSummary.surname}",
      createdBy = loggedInUser,
      providerCode = deliusStaff.probationArea!!.code,
      isActive = true
    )
    personManagerRepository.save(personManagerEntity)
    workloadCalculationService.calculate(deliusStaff.staffCode, teamCode, deliusStaff.grade)
    return SaveResult(personManagerEntity, true)
  }
}
