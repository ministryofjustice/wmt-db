package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTInstitutionalReportRepository

@Service
class WMTGetParoleReports(private val wmtInstitutionalReportRepository: WMTInstitutionalReportRepository) {
  fun getParoleReports(staffIdentifier: StaffIdentifier): Int =
    wmtInstitutionalReportRepository.findFirstByStaffCodeAndTeamCode(staffIdentifier.staffCode, staffIdentifier.teamCode)?.paroleReports ?: 0
}
