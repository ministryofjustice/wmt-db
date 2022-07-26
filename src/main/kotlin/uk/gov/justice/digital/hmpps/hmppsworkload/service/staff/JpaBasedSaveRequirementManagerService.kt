package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SuccessUpdater
import uk.gov.justice.digital.hmpps.hmppsworkload.service.TelemetryService
import javax.transaction.Transactional

@Service
class JpaBasedSaveRequirementManagerService(
  private val requirementManagerRepository: RequirementManagerRepository,
  private val telemetryService: TelemetryService,
  private val successUpdater: SuccessUpdater
) : SaveRequirementManagerService {

  @Transactional
  override fun saveRequirementManagers(
    teamCode: String,
    staff: Staff,
    allocateCase: AllocateCase,
    loggedInUser: String,
    requirements: List<ConvictionRequirement>
  ): List<SaveResult<RequirementManagerEntity>> {
    return requirements
      .filter { requirement -> requirement.requirementTypeMainCategory.code != "W" }
      .map { requirement ->
        (
          requirementManagerRepository.findFirstByCrnAndEventIdAndRequirementIdOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventId, requirement.requirementId)?.let { requirementManagerEntity ->
            if (requirementManagerEntity.staffId == staff.staffIdentifier && requirementManagerEntity.teamCode == teamCode) {
              SaveResult(requirementManagerEntity, false)
            } else {
              saveRequirementManagerEntity(allocateCase, staff, teamCode, loggedInUser, requirement)
            }
          } ?: saveRequirementManagerEntity(allocateCase, staff, teamCode, loggedInUser, requirement)
          ).also { savedRequirementManager ->
          successUpdater.updateRequirement(savedRequirementManager.entity.crn, savedRequirementManager.entity.uuid, savedRequirementManager.entity.createdDate!!)
        }
      }
  }

  private fun saveRequirementManagerEntity(
    allocateCase: AllocateCase,
    staff: Staff,
    teamCode: String,
    loggedInUser: String,
    requirement: ConvictionRequirement
  ): SaveResult<RequirementManagerEntity> {
    val requirementManagerEntity = RequirementManagerEntity(
      crn = allocateCase.crn,
      staffId = staff.staffIdentifier,
      staffCode = staff.staffCode,
      teamCode = teamCode,
      eventId = allocateCase.eventId,
      requirementId = requirement.requirementId,
      createdBy = loggedInUser,
      providerCode = staff.probationArea!!.code
    )
    requirementManagerRepository.save(requirementManagerEntity)
    telemetryService.trackRequirementManagerAllocated(requirementManagerEntity)
    return SaveResult(requirementManagerEntity, true)
  }
}
