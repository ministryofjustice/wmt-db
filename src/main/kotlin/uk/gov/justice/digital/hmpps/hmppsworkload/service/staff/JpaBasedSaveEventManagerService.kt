package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import javax.transaction.Transactional

@Service
class JpaBasedSaveEventManagerService(
  private val eventManagerRepository: EventManagerRepository
) : SaveEventManagerService {

  @Transactional
  override fun saveEventManager(
    teamCode: String,
    deliusStaff: DeliusStaff,
    allocateCase: AllocateCase,
    loggedInUser: String
  ): SaveResult<EventManagerEntity> = eventManagerRepository.findFirstByCrnAndEventIdOrderByCreatedDateDesc(allocateCase.crn, allocateCase.eventId)?.let { eventManager ->
    if (eventManager.staffId == deliusStaff.staffIdentifier && eventManager.teamCode == teamCode) {
      return SaveResult(eventManager, false)
    }
    eventManager.isActive = false
    saveEventManagerEntity(allocateCase, deliusStaff, teamCode, loggedInUser)
  } ?: saveEventManagerEntity(allocateCase, deliusStaff, teamCode, loggedInUser)

  private fun saveEventManagerEntity(
    allocateCase: AllocateCase,
    deliusStaff: DeliusStaff,
    teamCode: String,
    loggedInUser: String
  ): SaveResult<EventManagerEntity> {
    val eventManagerEntity = EventManagerEntity(
      crn = allocateCase.crn,
      staffId = deliusStaff.staffIdentifier,
      staffCode = deliusStaff.staffCode,
      teamCode = teamCode,
      eventId = allocateCase.eventId,
      createdBy = loggedInUser,
      providerCode = deliusStaff.probationArea!!.code,
      isActive = true
    )
    eventManagerRepository.save(eventManagerEntity)
    return SaveResult(eventManagerEntity, true)
  }
}
