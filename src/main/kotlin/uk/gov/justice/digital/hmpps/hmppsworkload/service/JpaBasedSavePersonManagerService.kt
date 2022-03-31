package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.error.PersonManagerAlreadyAllocatedError
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import java.math.BigInteger

@Service
class JpaBasedSavePersonManagerService(
  private val personManagerRepository: PersonManagerRepository,
  private val communityApiClient: CommunityApiClient
) : SavePersonManagerService {
  override fun savePersonManager(
    teamCode: String,
    staffId: BigInteger,
    allocateCase: AllocateCase,
    loggedInUser: String
  ): PersonManagerEntity =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(allocateCase.crn)?.let { personManager ->
      if (personManager.staffId == staffId && personManager.teamCode == teamCode) {
        return personManager
      }
      throw PersonManagerAlreadyAllocatedError("CRN ${allocateCase.crn} already allocated")
    } ?: run {
      val personManagerEntity = Mono.zip(communityApiClient.getStaffById(staffId), communityApiClient.getSummaryByCrn(allocateCase.crn))
        .map { results ->
          PersonManagerEntity(crn = allocateCase.crn, staffId = results.t1.staffIdentifier, staffCode = results.t1.staffCode, teamCode = teamCode, offenderName = "${results.t2.firstName} ${results.t2.surname}", createdBy = loggedInUser)
        }.block()!!
      personManagerRepository.save(personManagerEntity)
      personManagerEntity
    }
}
