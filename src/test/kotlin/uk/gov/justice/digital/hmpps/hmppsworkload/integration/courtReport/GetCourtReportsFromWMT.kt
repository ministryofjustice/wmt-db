package uk.gov.justice.digital.hmpps.hmppsworkload.integration.courtReport

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTCourtReportsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WMTGetCourtReports

class GetCourtReportsFromWMT : IntegrationTestBase() {

  @Autowired
  protected lateinit var wmtGetCourtReports: WMTGetCourtReports

  @Test
  fun `get standard delivery reports from completed in last 30 days`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val standardDeliveryReportCount = 10
    wmtCourtReportsRepository.save(WMTCourtReportsEntity(staffCode = staffCode, teamCode = teamCode, fastDeliveryReportCount = 0, standardDeliveryReportCount = standardDeliveryReportCount))

    val results = wmtGetCourtReports.getCourtReports(StaffIdentifier(staffCode, teamCode))

    Assertions.assertEquals(standardDeliveryReportCount, results.size)

    results.forEach { courtReport ->
      Assertions.assertEquals(CourtReportType.STANDARD, courtReport.type)
    }
  }

  @Test
  fun `return empty list if no court reports are found`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val results = wmtGetCourtReports.getCourtReports(StaffIdentifier(staffCode, teamCode))
    Assertions.assertTrue(results.isEmpty())
  }

  @Test
  fun `return empty list if court report counts are null`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    wmtCourtReportsRepository.save(WMTCourtReportsEntity(staffCode = staffCode, teamCode = teamCode))
    val results = wmtGetCourtReports.getCourtReports(StaffIdentifier(staffCode, teamCode))
    Assertions.assertTrue(results.isEmpty())
  }

  @Test
  fun `get fast delivery reports from conversions in last 30 days`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val fastDeliveryReportCount = 10
    wmtCourtReportsRepository.save(WMTCourtReportsEntity(staffCode = staffCode, teamCode = teamCode, fastDeliveryReportCount = fastDeliveryReportCount, standardDeliveryReportCount = 0))

    val results = wmtGetCourtReports.getCourtReports(StaffIdentifier(staffCode, teamCode))

    Assertions.assertEquals(fastDeliveryReportCount, results.size)

    results.forEach { courtReport ->
      Assertions.assertEquals(CourtReportType.FAST, courtReport.type)
    }
  }
}
