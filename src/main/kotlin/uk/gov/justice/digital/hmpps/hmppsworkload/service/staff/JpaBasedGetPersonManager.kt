package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import java.util.UUID

@Service
class JpaBasedGetPersonManager(
  private val personManagerRepository: PersonManagerRepository,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient
) : GetPersonManager {
  override fun findById(id: UUID): PersonManagerDetails? = personManagerRepository.findByUuid(id)?.let { entity ->
    val staff = communityApiClient.getStaffByCode(entity.staffCode).block()!!
    PersonManagerDetails.from(entity, staff)
  }

  override fun findLatestByCrn(crn: String): PersonManager? {
    val personManager = personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)
    if (personManager != null) {
      val staff = communityApiClient.getStaffByCode(personManager.staffCode).block()
      return PersonManager(personManager.staffCode, personManager.teamCode, staff!!.grade)
    }
    return null
  }
}
