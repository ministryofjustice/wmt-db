package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import java.util.UUID

@Service
class JpaBasedGetPersonManager(
  private val personManagerRepository: PersonManagerRepository,
  private val getStaffService: GetStaffService
) : GetPersonManager {
  override fun findById(id: UUID): PersonManagerDetails? = personManagerRepository.findByUuid(id)?.let { entity ->
    val staff = getStaffService.getStaffById(entity.staffId)!!
    PersonManagerDetails.from(entity, staff)
  }

  override fun findLatestByCrn(crn: String): PersonManager? {
    val personManager = personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)
    if (personManager != null) {
      val staff = getStaffService.getStaffByCode(personManager.staffCode)
      return PersonManager(personManager.staffCode, personManager.teamCode, personManager.providerCode, staff!!.grade)
    }
    return null
  }
}
