package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTInstitutionalReportRepository

@Service
class WMTGetInstitutionalReports(private val wmtInstitutionalReportRepository: WMTInstitutionalReportRepository) :
  GetInstitutionalReports {
  override fun getInstitutionalReports(staffCode: String, teamCode: String): Int =
    wmtInstitutionalReportRepository.findFirstByTeamCodeAndStaffCode(teamCode, staffCode)?.let { it.paroleReports } ?: 0
}
