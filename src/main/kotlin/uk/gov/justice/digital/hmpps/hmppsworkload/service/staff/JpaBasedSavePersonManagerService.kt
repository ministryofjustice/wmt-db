package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
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
  /***
   * Recalculate workload of existing and new case managers.
   */
  override fun savePersonManager(
    teamCode: String,
    staff: Staff,
    loggedInUser: String,
    personSummary: PersonSummary,
    crn: String
  ): SaveResult<PersonManagerEntity> =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)?.let { existingPersonManager ->
      if (existingPersonManager.staffId == staff.staffIdentifier && existingPersonManager.teamCode == teamCode) {
        SaveResult(existingPersonManager, false)
      } else {
        val currentPersonManager = getPersonManager.findLatestByCrn(crn)
        createPersonManager(staff, teamCode, personSummary, loggedInUser, crn).also {
          existingPersonManager.isActive = false
          workloadCalculationService.calculate(currentPersonManager!!.staffCode, currentPersonManager.teamCode, currentPersonManager.staffGrade)
        }
      }
    } ?: createPersonManager(staff, teamCode, personSummary, loggedInUser, crn)

  private fun createPersonManager(
    staff: Staff,
    teamCode: String,
    personSummary: PersonSummary,
    loggedInUser: String,
    crn: String
  ): SaveResult<PersonManagerEntity> {
    val personManagerEntity = PersonManagerEntity(
      crn = crn,
      staffId = staff.staffIdentifier,
      staffCode = staff.staffCode,
      teamCode = teamCode,
      offenderName = "${personSummary.firstName} ${personSummary.surname}",
      createdBy = loggedInUser,
      providerCode = staff.probationArea!!.code,
      isActive = true
    )
    personManagerRepository.save(personManagerEntity)
    workloadCalculationService.calculate(staff.staffCode, teamCode, staff.grade)
    return SaveResult(personManagerEntity, true)
  }
}
