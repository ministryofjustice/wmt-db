package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTInstitutionalReportRepository

@Service
class WMTGetParoleReports(private val wmtInstitutionalReportRepository: WMTInstitutionalReportRepository) :
  GetParoleReports {
  override fun getParoleReports(staffCode: String, teamCode: String): Int =
    wmtInstitutionalReportRepository.findFirstByStaffCodeAndTeamCode(staffCode, teamCode)?.paroleReports ?: 0
}
