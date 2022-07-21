package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
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
  ): List<RequirementManagerEntity> {
    return requirements
      .filter { requirement -> requirement.requirementTypeMainCategory.code != "W" }
      .map { requirement ->
        requirementManagerRepository.findFirstByCrnAndEventIdAndRequirementIdOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventId, requirement.requirementId)?.let { requirementManagerEntity ->
          if (requirementManagerEntity.staffId == staff.staffIdentifier && requirementManagerEntity.teamCode == teamCode) {
            requirementManagerEntity
          } else {
            saveRequirementManagerEntity(allocateCase, staff, teamCode, loggedInUser, requirement)
          }
        } ?: run {
          saveRequirementManagerEntity(allocateCase, staff, teamCode, loggedInUser, requirement)
        }
      }
  }

  private fun saveRequirementManagerEntity(
    allocateCase: AllocateCase,
    staff: Staff,
    teamCode: String,
    loggedInUser: String,
    requirement: ConvictionRequirement
  ): RequirementManagerEntity {
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
    successUpdater.updateRequirement(requirementManagerEntity.crn, requirementManagerEntity.uuid, requirementManagerEntity.createdDate!!)
    return requirementManagerEntity
  }
}
