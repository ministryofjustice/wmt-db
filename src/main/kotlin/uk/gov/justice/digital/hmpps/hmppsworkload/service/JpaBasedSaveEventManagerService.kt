package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository

@Service
class JpaBasedSaveEventManagerService(
  private val eventManagerRepository: EventManagerRepository
) : SaveEventManagerService {

  override fun saveEventManager(
    teamCode: String,
    staff: Staff,
    allocateCase: AllocateCase,
    loggedInUser: String
  ): EventManagerEntity = eventManagerRepository.findFirstByCrnAndEventIdOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventId)?.let { eventManager ->
    if (eventManager.staffId == staff.staffIdentifier && eventManager.teamCode == teamCode) {
      return eventManager
    }
    saveEventManagerEntity(allocateCase, staff, teamCode, loggedInUser)
  } ?: run {
    saveEventManagerEntity(allocateCase, staff, teamCode, loggedInUser)
  }

  private fun saveEventManagerEntity(
    allocateCase: AllocateCase,
    staff: Staff,
    teamCode: String,
    loggedInUser: String
  ): EventManagerEntity {
    val eventManagerEntity = EventManagerEntity(
      crn = allocateCase.crn,
      staffId = staff.staffIdentifier,
      staffCode = staff.staffCode,
      teamCode = teamCode,
      eventId = allocateCase.eventId,
      createdBy = loggedInUser
    )
    eventManagerRepository.save(eventManagerEntity)
    return eventManagerEntity
  }
}
