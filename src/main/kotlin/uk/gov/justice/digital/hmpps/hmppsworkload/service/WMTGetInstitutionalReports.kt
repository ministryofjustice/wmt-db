package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReportType.PAROLE_REPORT
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTInstitutionalReportRepository

@Service
class WMTGetInstitutionalReports(private val wmtInstitutionalReportRepository: WMTInstitutionalReportRepository) :
  GetInstitutionalReports {
  override fun getInstitutionalReports(staffCode: String, teamCode: String): List<InstitutionalReport> =
    wmtInstitutionalReportRepository.findByTeamCodeAndStaffCode(teamCode, staffCode)
      .map { InstitutionalReport(PAROLE_REPORT) }
}
