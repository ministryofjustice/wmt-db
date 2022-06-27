package uk.gov.justice.digital.hmpps.hmppsworkload.integration.workloadCalc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import java.math.BigInteger

internal class WorkloadCalculationServiceTest : IntegrationTestBase() {

  @Autowired
  protected lateinit var workloadCalculation: WorkloadCalculationService

  @Test
  fun `can calculate WorkloadCalculation`() {

    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val providerCode = "SC1"
    val staffGrade = "PO"
    val workloadOwnerId = 5L

    workloadCalculation.calculateWorkloadCalculation(staffCode, teamCode, providerCode, staffGrade, workloadOwnerId)

    Assertions.assertEquals(
      BigInteger.ZERO,
      workloadCalculationRepository
        .findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(staffCode, teamCode)?.workloadPoints
    )
  }
}
