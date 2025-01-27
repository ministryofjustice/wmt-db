package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCourtReportsRepository
import java.util.stream.IntStream

@Service
class WMTGetCourtReports(private val wmtCourtReportsRepository: WMTCourtReportsRepository) {
  fun getCourtReports(staffIdentifier: StaffIdentifier): List<CourtReport> = wmtCourtReportsRepository.findByStaffCodeAndTeamCode(staffIdentifier.staffCode, staffIdentifier.teamCode)?.let { wmtCourtReportsEntity ->
    return countToCourtReports(wmtCourtReportsEntity.standardDeliveryReportCount, CourtReportType.STANDARD) +
      countToCourtReports(wmtCourtReportsEntity.fastDeliveryReportCount, CourtReportType.FAST)
  } ?: emptyList()

  private fun countToCourtReports(count: Int?, type: CourtReportType): List<CourtReport> = IntStream.range(0, count ?: 0).mapToObj {
    CourtReport(type)
  }.toList()
}
