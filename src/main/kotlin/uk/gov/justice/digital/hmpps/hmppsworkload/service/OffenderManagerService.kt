package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper

@Service
class OffenderManagerService(
  private val personManagerRepository: PersonManagerRepository,
  private val getStaffService: GetStaffService,
  private val gradeMapper: GradeMapper
) {

  fun getByCrn(crn: String): PersonManager? {
    val personManager = personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(crn)
    if (personManager != null) {
      val staff: Staff? = getStaffService.getStaffById(personManager.staffId)
      return PersonManager(personManager.staffCode, personManager.teamCode, personManager.providerCode, gradeMapper.deliusToStaffGrade(staff?.staffGrade!!.code))
    }
    return null
  }
}
