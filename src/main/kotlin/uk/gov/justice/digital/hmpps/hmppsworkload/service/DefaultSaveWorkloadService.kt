package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.SaveEventManagerService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.SavePersonManagerService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.SaveRequirementManagerService
import java.math.BigInteger

@Service
class DefaultSaveWorkloadService(
  private val savePersonManagerService: SavePersonManagerService,
  private val communityApiClient: CommunityApiClient,
  private val saveEventManagerService: SaveEventManagerService,
  private val saveRequirementManagerService: SaveRequirementManagerService,
  private val notificationService: NotificationService
) : SaveWorkloadService {

  override fun saveWorkload(
    teamCode: String,
    staffId: BigInteger,
    allocateCase: AllocateCase,
    loggedInUser: String,
    authToken: String
  ): CaseAllocated {
    val staff = communityApiClient.getStaffById(staffId).block()!!
    val summary = communityApiClient.getSummaryByCrn(allocateCase.crn).block()!!
    val activeRequirements = communityApiClient.getActiveRequirements(allocateCase.crn, allocateCase.eventId).block()!!.requirements
    val personManagerId = savePersonManagerService.savePersonManager(teamCode, staff, allocateCase, loggedInUser, summary).uuid
    val eventManagerId = saveEventManagerService.saveEventManager(teamCode, staff, allocateCase, loggedInUser).uuid
    val requirementManagerIds = saveRequirementManagerService.saveRequirementManagers(teamCode, staff, allocateCase, loggedInUser, activeRequirements)
    notificationService.notifyAllocation(staff, summary, activeRequirements, allocateCase, loggedInUser, authToken).block()

    return CaseAllocated(personManagerId, eventManagerId, requirementManagerIds.map { it.uuid })
  }
}
