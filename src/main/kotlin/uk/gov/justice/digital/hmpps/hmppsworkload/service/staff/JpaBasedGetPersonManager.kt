package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import java.util.UUID

@Service
class JpaBasedGetPersonManager(
  private val personManagerRepository: PersonManagerRepository,
  @Qualifier("workforceAllocationsToDeliusApiClient") private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient
) : GetPersonManager {
  override fun findById(id: UUID): PersonManagerDetails? = personManagerRepository.findByUuid(id)?.let { entity ->
    PersonManagerDetails.from(entity)
  }

  override suspend fun findLatestByCrn(crn: String): PersonManager? {
    val personManager = personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)
    if (personManager != null) {
      val staff = workforceAllocationsToDeliusApiClient.getOfficerView(personManager.staffCode)
      return PersonManager(personManager.staffCode, personManager.teamCode, staff.getGrade())
    }
    return null
  }
}
