package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
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
    staff: Staff,
    allocateCase: AllocateCase,
    loggedInUser: String,
    personSummary: PersonSummary
  ): SaveResult<PersonManagerEntity> =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(allocateCase.crn)?.let { personManager ->
      if (personManager.staffId == staff.staffIdentifier && personManager.teamCode == teamCode) {
        SaveResult(personManager, false)
      } else {
        val currentPersonManager = getPersonManager.findLatestByCrn(allocateCase.crn)
        createPersonManagerEntityAndSendSQSMessage(allocateCase, staff, teamCode, personSummary, loggedInUser).also {
          workloadCalculationService.calculate(currentPersonManager!!.staffCode, currentPersonManager.teamCode, currentPersonManager.providerCode, currentPersonManager.staffGrade)
        }
      }
    } ?: createPersonManagerEntityAndSendSQSMessage(allocateCase, staff, teamCode, personSummary, loggedInUser)

  private fun createPersonManagerEntityAndSendSQSMessage(
    allocateCase: AllocateCase,
    staff: Staff,
    teamCode: String,
    personSummary: PersonSummary,
    loggedInUser: String
  ): SaveResult<PersonManagerEntity> {
    val providerCode = staff.probationArea!!.code
    val personManagerEntity = PersonManagerEntity(
      crn = allocateCase.crn,
      staffId = staff.staffIdentifier,
      staffCode = staff.staffCode,
      teamCode = teamCode,
      offenderName = "${personSummary.firstName} ${personSummary.surname}",
      createdBy = loggedInUser,
      providerCode = providerCode
    )
    personManagerRepository.save(personManagerEntity)
    workloadCalculationService.calculate(staff.staffCode, teamCode, providerCode, staff.grade)
    return SaveResult(personManagerEntity, true)
  }
}
