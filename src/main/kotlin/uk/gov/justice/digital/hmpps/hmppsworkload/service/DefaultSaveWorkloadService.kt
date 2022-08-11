package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.SaveEventManagerService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.SavePersonManagerService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.SaveRequirementManagerService

@Service
class DefaultSaveWorkloadService(
  private val savePersonManagerService: SavePersonManagerService,
  private val communityApiClient: CommunityApiClient,
  private val saveEventManagerService: SaveEventManagerService,
  private val saveRequirementManagerService: SaveRequirementManagerService,
  private val notificationService: NotificationService,
  private val telemetryService: TelemetryService,
  private val successUpdater: SuccessUpdater,
) : SaveWorkloadService {

  override fun saveWorkload(
    teamCode: String,
    staffCode: String,
    allocateCase: AllocateCase,
    loggedInUser: String,
    authToken: String
  ): CaseAllocated {
    val staff = communityApiClient.getStaffByCode(staffCode).block()!!
    val summary = communityApiClient.getSummaryByCrn(allocateCase.crn).block()!!
    val activeRequirements = communityApiClient.getActiveRequirements(allocateCase.crn, allocateCase.eventId).block()!!.requirements
    val personManagerSaveResult = savePersonManagerService.savePersonManager(teamCode, staff, allocateCase, loggedInUser, summary).also { afterPersonManagerSaved(it) }
    val eventManagerSaveResult = saveEventManagerService.saveEventManager(teamCode, staff, allocateCase, loggedInUser).also { afterEventManagerSaved(it) }
    val requirementManagerSaveResults = saveRequirementManagerService.saveRequirementManagers(teamCode, staff, allocateCase, loggedInUser, activeRequirements).also { afterRequirementManagersSaved(it) }
    if (personManagerSaveResult.hasChanged || eventManagerSaveResult.hasChanged || requirementManagerSaveResults.any { it.hasChanged }) {
      notificationService.notifyAllocation(staff, summary, activeRequirements, allocateCase, loggedInUser, authToken)
        .block()
    }

    return CaseAllocated(personManagerSaveResult.entity.uuid, eventManagerSaveResult.entity.uuid, requirementManagerSaveResults.map { it.entity.uuid })
  }

  private fun afterPersonManagerSaved(personManagerSaveResult: SaveResult<PersonManagerEntity>) {
    if (personManagerSaveResult.hasChanged) {
      telemetryService.trackPersonManagerAllocated(personManagerSaveResult.entity)
      successUpdater.updatePerson(
        personManagerSaveResult.entity.crn,
        personManagerSaveResult.entity.uuid,
        personManagerSaveResult.entity.createdDate!!
      )
    }
  }

  private fun afterEventManagerSaved(eventManagerSaveResult: SaveResult<EventManagerEntity>) {
    if (eventManagerSaveResult.hasChanged) {
      telemetryService.trackEventManagerAllocated(eventManagerSaveResult.entity)
      successUpdater.updateEvent(eventManagerSaveResult.entity.crn, eventManagerSaveResult.entity.uuid, eventManagerSaveResult.entity.createdDate!!)
    }
  }

  private fun afterRequirementManagersSaved(requirementManagerSaveResults: List<SaveResult<RequirementManagerEntity>>) {
    requirementManagerSaveResults.filter { it.hasChanged }.forEach { saveResult ->
      telemetryService.trackRequirementManagerAllocated(saveResult.entity)
      successUpdater.updateRequirement(saveResult.entity.crn, saveResult.entity.uuid, saveResult.entity.createdDate!!)
    }
  }
}
