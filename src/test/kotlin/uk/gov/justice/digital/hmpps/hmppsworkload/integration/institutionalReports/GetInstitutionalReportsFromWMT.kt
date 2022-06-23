package uk.gov.justice.digital.hmpps.hmppsworkload.integration.institutionalReports

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReportType.PAROLE_REPORT
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

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(PAROLE_REPORT, results[0].type)
  }
}
