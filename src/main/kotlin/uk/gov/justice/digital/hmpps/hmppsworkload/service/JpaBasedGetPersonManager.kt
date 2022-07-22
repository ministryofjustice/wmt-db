package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import java.util.UUID

@Service
class JpaBasedGetPersonManager(
  private val personManagerRepository: PersonManagerRepository,
  private val communityApiClient: CommunityApiClient
) : GetPersonManager {
  override fun findById(id: UUID): PersonManagerDetails? = personManagerRepository.findByUuid(id)?.let { entity ->
    val staff = communityApiClient.getStaffById(entity.staffId).block()!!
    PersonManagerDetails.from(entity, staff)
  }
}
