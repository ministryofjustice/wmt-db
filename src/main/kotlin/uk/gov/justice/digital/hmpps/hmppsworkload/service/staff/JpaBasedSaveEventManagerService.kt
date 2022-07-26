package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SuccessUpdater
import uk.gov.justice.digital.hmpps.hmppsworkload.service.TelemetryService
import javax.transaction.Transactional

@Service
class JpaBasedSaveEventManagerService(
  private val eventManagerRepository: EventManagerRepository,
  private val telemetryService: TelemetryService,
  private val successUpdater: SuccessUpdater
) : SaveEventManagerService {

  @Transactional
  override fun saveEventManager(
    teamCode: String,
    staff: Staff,
    allocateCase: AllocateCase,
    loggedInUser: String
  ): SaveResult<EventManagerEntity> = eventManagerRepository.findFirstByCrnAndEventIdOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventId)?.let { eventManager ->
    if (eventManager.staffId == staff.staffIdentifier && eventManager.teamCode == teamCode) {
      return SaveResult(eventManager, false)
    }
    saveEventManagerEntity(allocateCase, staff, teamCode, loggedInUser)
  } ?: saveEventManagerEntity(allocateCase, staff, teamCode, loggedInUser)

  private fun saveEventManagerEntity(
    allocateCase: AllocateCase,
    staff: Staff,
    teamCode: String,
    loggedInUser: String
  ): SaveResult<EventManagerEntity> {
    val eventManagerEntity = EventManagerEntity(
      crn = allocateCase.crn,
      staffId = staff.staffIdentifier,
      staffCode = staff.staffCode,
      teamCode = teamCode,
      eventId = allocateCase.eventId,
      createdBy = loggedInUser,
      providerCode = staff.probationArea!!.code
    )
    eventManagerRepository.save(eventManagerEntity)
    telemetryService.trackEventManagerAllocated(eventManagerEntity)
    successUpdater.updateEvent(eventManagerEntity.crn, eventManagerEntity.uuid, eventManagerEntity.createdDate!!)
    return SaveResult(eventManagerEntity, true)
  }
}
