package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper

@Service
class JpaBasedSavePersonManagerService(
  private val personManagerRepository: PersonManagerRepository,
  private val telemetryService: TelemetryService,
  private val successUpdater: SuccessUpdater,
  private val workloadCalculationService: WorkloadCalculationService,
  private val offenderManagerService: OffenderManagerService,
  private val gradeMapper: GradeMapper
) : SavePersonManagerService {
  override fun savePersonManager(
    teamCode: String,
    staff: Staff,
    allocateCase: AllocateCase,
    loggedInUser: String,
    personSummary: PersonSummary
  ): PersonManagerEntity =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(allocateCase.crn)?.let { personManager ->
      if (personManager.staffId == staff.staffIdentifier && personManager.teamCode == teamCode) {
        personManager
      } else {
        val oldOffenderManager = offenderManagerService.getByCrn(allocateCase.crn)
        createPersonManagerEntityAndSendSQSMessage(allocateCase, staff, teamCode, personSummary, loggedInUser).also {
          workloadCalculationService.calculate(oldOffenderManager!!.staffCode, oldOffenderManager.teamCode, oldOffenderManager.providerCode, oldOffenderManager.staffGrade)
        }
      }
    } ?: createPersonManagerEntityAndSendSQSMessage(allocateCase, staff, teamCode, personSummary, loggedInUser)

  private fun createPersonManagerEntityAndSendSQSMessage(
    allocateCase: AllocateCase,
    staff: Staff,
    teamCode: String,
    personSummary: PersonSummary,
    loggedInUser: String
  ): PersonManagerEntity {
    val personManagerEntity = PersonManagerEntity(
      crn = allocateCase.crn,
      staffId = staff.staffIdentifier,
      staffCode = staff.staffCode,
      teamCode = teamCode,
      offenderName = "${personSummary.firstName} ${personSummary.surname}",
      createdBy = loggedInUser,
      providerCode = staff.probationArea!!.code
    )
    personManagerRepository.save(personManagerEntity)
    telemetryService.trackPersonManagerAllocated(personManagerEntity)
    successUpdater.updatePerson(personManagerEntity.crn, personManagerEntity.uuid, personManagerEntity.createdDate!!)
    workloadCalculationService.calculate(staff.staffCode, teamCode, staff.probationArea?.code ?: "", gradeMapper.deliusToStaffGrade(staff.staffGrade?.code ?: ""))
    return personManagerEntity
  }
}
