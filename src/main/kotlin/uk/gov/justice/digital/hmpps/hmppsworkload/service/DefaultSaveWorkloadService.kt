package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated
import java.math.BigInteger
import javax.transaction.Transactional

@Service
class DefaultSaveWorkloadService(
  private val savePersonManagerService: SavePersonManagerService,
  private val communityApiClient: CommunityApiClient,
  private val saveEventManagerService: SaveEventManagerService,
  private val saveRequirementManagerService: SaveRequirementManagerService,
  private val notificationService: NotificationService,
  private val workloadCalculationService: WorkloadCalculationService
) : SaveWorkloadService {
  @Transactional
  override fun saveWorkload(
    teamCode: String,
    staffId: BigInteger,
    allocateCase: AllocateCase,
    loggedInUser: String,
    authToken: String
  ): CaseAllocated {
    return Mono.zip(
      communityApiClient.getStaffById(staffId), communityApiClient.getSummaryByCrn(allocateCase.crn), communityApiClient.getActiveRequirements(allocateCase.crn, allocateCase.eventId)
    )
      .flatMap { results ->
        val staff = results.t1
        val personManagerId = savePersonManagerService.savePersonManager(teamCode, staff, allocateCase, loggedInUser, results.t2).uuid
        val eventManagerId = saveEventManagerService.saveEventManager(teamCode, staff, allocateCase, loggedInUser).uuid
        val requirementManagerIds = saveRequirementManagerService.saveRequirementManagers(teamCode, staff, allocateCase, loggedInUser, results.t3.requirements)

        workloadCalculationService.calculate(staff.staffCode, teamCode, staff.probationArea?.code ?: "", staff.staffGrade?.code ?: "")

        notificationService.notifyAllocation(staff, results.t2, results.t3.requirements, allocateCase, loggedInUser, teamCode, authToken)
          .map { CaseAllocated(personManagerId, eventManagerId, requirementManagerIds.map { it.uuid }) }
      }.block()!!
  }
}
