package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import javax.transaction.Transactional

@Service
class JpaBasedSaveRequirementManagerService(
  private val requirementManagerRepository: RequirementManagerRepository
) : SaveRequirementManagerService {

  @Transactional
  override fun saveRequirementManagers(
    teamCode: String,
    deliusStaff: DeliusStaff,
    allocateCase: AllocateCase,
    loggedInUser: String,
    requirements: List<ConvictionRequirement>
  ): List<SaveResult<RequirementManagerEntity>> {
    return requirements
      .filter { requirement -> requirement.requirementTypeMainCategory.code != "W" }
      .map { requirement ->
        requirementManagerRepository.findFirstByCrnAndEventNumberAndRequirementIdOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventNumber, requirement.requirementId)?.let { requirementManagerEntity ->
          if (requirementManagerEntity.staffCode == deliusStaff.staffCode && requirementManagerEntity.teamCode == teamCode) {
            SaveResult(requirementManagerEntity, false)
          } else {
            requirementManagerEntity.isActive = false
            saveRequirementManagerEntity(allocateCase, deliusStaff, teamCode, loggedInUser, requirement)
          }
        } ?: saveRequirementManagerEntity(allocateCase, deliusStaff, teamCode, loggedInUser, requirement)
      }
  }

  private fun saveRequirementManagerEntity(
    allocateCase: AllocateCase,
    deliusStaff: DeliusStaff,
    teamCode: String,
    loggedInUser: String,
    requirement: ConvictionRequirement
  ): SaveResult<RequirementManagerEntity> {
    val requirementManagerEntity = RequirementManagerEntity(
      crn = allocateCase.crn,
      staffId = deliusStaff.staffIdentifier,
      staffCode = deliusStaff.staffCode,
      teamCode = teamCode,
      eventId = allocateCase.eventId,
      requirementId = requirement.requirementId,
      createdBy = loggedInUser,
      providerCode = deliusStaff.probationArea!!.code,
      isActive = true,
      eventNumber = allocateCase.eventNumber
    )
    requirementManagerRepository.save(requirementManagerEntity)
    return SaveResult(requirementManagerEntity, true)
  }
}
