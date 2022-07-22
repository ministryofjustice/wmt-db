package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository

@Service
class OffenderManagerService(
  private val personManagerRepository: PersonManagerRepository,
  private val getStaffService: GetStaffService
) {

  fun getByCrn(crn: String): PersonManager? {
    val personManager = personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)
    if (personManager != null) {
      val staff = getStaffService.getStaffByCode(personManager.staffCode)
      return PersonManager(personManager.staffCode, personManager.teamCode, personManager.providerCode, staff!!.grade)
    }
    return null
  }
}
