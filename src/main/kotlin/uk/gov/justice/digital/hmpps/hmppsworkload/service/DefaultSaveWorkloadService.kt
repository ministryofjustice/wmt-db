package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated
import java.math.BigInteger
import javax.transaction.Transactional

@Service
class DefaultSaveWorkloadService(private val savePersonManagerService: SavePersonManagerService, private val communityApiClient: CommunityApiClient, private val saveEventManagerService: SaveEventManagerService) : SaveWorkloadService {
  @Transactional
  override fun saveWorkload(
    teamCode: String,
    staffId: BigInteger,
    allocateCase: AllocateCase,
    loggedInUser: String
  ): CaseAllocated {
    return Mono.zip(communityApiClient.getStaffById(staffId), communityApiClient.getSummaryByCrn(allocateCase.crn))
      .map { results ->
        val personManagerId = savePersonManagerService.savePersonManager(teamCode, results.t1, allocateCase, loggedInUser, results.t2).uuid
        val eventManagerId = saveEventManagerService.saveEventManager(teamCode, results.t1, allocateCase, loggedInUser).uuid
        CaseAllocated(personManagerId, eventManagerId)
      }.block()!!
  }
}
