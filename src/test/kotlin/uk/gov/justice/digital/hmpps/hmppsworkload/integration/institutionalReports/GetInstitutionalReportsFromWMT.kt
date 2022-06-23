package uk.gov.justice.digital.hmpps.hmppsworkload.integration.institutionalReports

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTInstitutionalReportEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WMTGetInstitutionalReports

class GetInstitutionalReportsFromWMT : IntegrationTestBase() {

  @Autowired
  lateinit var getInstitutionalReports: WMTGetInstitutionalReports

  @Test
  fun `must return parole reports`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    wmtInstitutionalReportRepository.save(WMTInstitutionalReportEntity(staffCode = staffCode, teamCode = teamCode, paroleReports = 1))

    val results = getInstitutionalReports.getInstitutionalReports(staffCode, teamCode)

    Assertions.assertEquals(1, results)
  }

  @Test
  fun `must return multiple parole reports`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    wmtInstitutionalReportRepository.save(WMTInstitutionalReportEntity(staffCode = staffCode, teamCode = teamCode, paroleReports = 3))

    val results = getInstitutionalReports.getInstitutionalReports(staffCode, teamCode)

    Assertions.assertEquals(3, results)
  }

  @Test
  fun `must handle null`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    wmtInstitutionalReportRepository.save(WMTInstitutionalReportEntity(staffCode = staffCode, teamCode = teamCode, paroleReports = null))

    val results = getInstitutionalReports.getInstitutionalReports(staffCode, teamCode)

    Assertions.assertEquals(0, results)
  }
}
