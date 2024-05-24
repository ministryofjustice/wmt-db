package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffMember
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerAuditEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerAuditRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository

@Service
class JpaBasedSaveEventManagerService(
  private val eventManagerRepository: EventManagerRepository,
  private val eventManagerAuditRepository: EventManagerAuditRepository,
) : SaveEventManagerService {

  @Transactional
  /***
   * if the case has an event manager check if the new event manager is the same otherwise make the older event manager
   * inactive and save the new event manager.
   */
  override fun saveEventManager(teamCode: String, deliusStaff: StaffMember, allocateCase: AllocateCase, loggedInUser: String, spoStaffId: String, spoName: String): SaveResult<EventManagerEntity> = eventManagerRepository.findFirstByCrnAndEventNumberOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventNumber)?.let { eventManager ->
    if (eventManager.staffCode == deliusStaff.code && eventManager.teamCode == teamCode) {
      return SaveResult(eventManager, false)
    }
    eventManager.isActive = false
    eventManagerRepository.save(eventManager)
    saveEventManagerEntity(allocateCase, deliusStaff, teamCode, loggedInUser, spoStaffId, spoName)
  } ?: saveEventManagerEntity(allocateCase, deliusStaff, teamCode, loggedInUser, spoStaffId, spoName)

  private fun saveEventManagerEntity(
    allocateCase: AllocateCase,
    deliusStaff: StaffMember,
    teamCode: String,
    loggedInUser: String,
    spoStaffId: String?,
    spoName: String?,
  ): SaveResult<EventManagerEntity> {
    val eventManagerEntity = EventManagerEntity(
      crn = allocateCase.crn,
      staffCode = deliusStaff.code,
      teamCode = teamCode,
      createdBy = loggedInUser,
      isActive = true,
      eventNumber = allocateCase.eventNumber,
      spoStaffId = spoStaffId,
      spoName = spoName,
    )
    eventManagerRepository.save(eventManagerEntity)
    auditEventManagerAllocation(allocateCase, loggedInUser, eventManagerEntity)
    return SaveResult(eventManagerEntity, true)
  }

  private fun auditEventManagerAllocation(allocateCase: AllocateCase, loggedInUser: String, eventManagerEntity: EventManagerEntity) {
    eventManagerAuditRepository.save(
      EventManagerAuditEntity(
        allocationJustificationNotes = allocateCase.allocationJustificationNotes,
        sensitiveNotes = allocateCase.sensitiveNotes,
        createdBy = loggedInUser,
        eventManager = eventManagerEntity,
      ),
    )
  }
}
