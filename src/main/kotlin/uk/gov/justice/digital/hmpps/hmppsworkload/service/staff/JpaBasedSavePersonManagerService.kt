package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
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
    crn: String
  ): SaveResult<PersonManagerEntity> =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)?.let { personManager ->
      if (personManager.staffCode == deliusStaff.staffCode && personManager.teamCode == teamCode) {
        SaveResult(personManager, false)
      } else {
        val currentPersonManager = getPersonManager.findLatestByCrn(crn)
        createPersonManager(deliusStaff, teamCode, loggedInUser, crn).also {
          personManager.isActive = false
          workloadCalculationService.saveWorkloadCalculation(
            StaffIdentifier(currentPersonManager!!.staffCode, currentPersonManager.teamCode),
            currentPersonManager.staffGrade
          )
        }
      }
    } ?: createPersonManager(deliusStaff, teamCode, loggedInUser, crn)

  private fun createPersonManager(
    deliusStaff: DeliusStaff,
    teamCode: String,
    loggedInUser: String,
    crn: String
  ): SaveResult<PersonManagerEntity> {
    val personManagerEntity = PersonManagerEntity(
      crn = crn,
      staffCode = deliusStaff.staffCode,
      teamCode = teamCode,
      createdBy = loggedInUser,
      isActive = true
    )
    personManagerRepository.save(personManagerEntity)
    workloadCalculationService.saveWorkloadCalculation(StaffIdentifier(deliusStaff.staffCode, teamCode), deliusStaff.grade)
    return SaveResult(personManagerEntity, true)
  }
}
