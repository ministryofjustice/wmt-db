package uk.gov.justice.digital.hmpps.hmppsworkload.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CommunityTierPoints
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CustodyTierPoints
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.LicenseTierPoints
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import java.math.BigDecimal
import java.math.BigInteger
import java.time.ZonedDateTime

class WorkloadCalculatorTests {
  private val workloadPointsRepository = mockk<WorkloadPointsRepository>()
  private val workloadCalculator = DefaultWorkloadCalculator(workloadPointsRepository)

  @Test
  fun `must sum all cases depending on their tier, category and whether they are T2A or not`() {
    val communityTierPoints = generateCommunityTierPoints()
    val licenseTierPoints = generateLicenseTierPoints()
    val custodyTierPoints = generateCustodyTierPoints()
    val t2aWorkloadPoints = WorkloadPointsEntity(null, communityTierPoints, licenseTierPoints, custodyTierPoints, ZonedDateTime.now(), ZonedDateTime.now(), true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigInteger.ZERO, BigInteger.ZERO)
    val workloadPoints = WorkloadPointsEntity(null, communityTierPoints, licenseTierPoints, custodyTierPoints, ZonedDateTime.now(), ZonedDateTime.now(), true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigInteger.ZERO, BigInteger.ZERO)
    every { workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true) } returns t2aWorkloadPoints
    every { workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false) } returns workloadPoints
    val numberOfT2aCases = 10
    val numberOfCases = 5
    val t2aCases = (1..numberOfT2aCases).map { Case(Tier.B2, CaseType.COMMUNITY, true) }
    val cases = (1..numberOfCases).map { Case(Tier.C1, CaseType.CUSTODY, false) }
    val result = workloadCalculator.getWorkloadPoints(t2aCases + cases, emptyList())
    val t2aExpectedWorkloadPoints = t2aWorkloadPoints.communityTierPoints.B2Points.multiply(numberOfT2aCases.toBigInteger())
    val casesExpectedWorkloadPoints = workloadPoints.custodyTierPoints.C1Points.multiply(numberOfCases.toBigInteger())
    Assertions.assertEquals(t2aExpectedWorkloadPoints.add(casesExpectedWorkloadPoints), result)
  }

  @Test
  fun `must sum all court reports`() {
    val standardCourtReportWeighting = BigInteger.TEN
    val fastCourtReportWeighting = BigInteger.TWO
    val t2aWorkloadPoints = WorkloadPointsEntity(null, generateCommunityTierPoints(), generateLicenseTierPoints(), generateCustodyTierPoints(), ZonedDateTime.now(), ZonedDateTime.now(), true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigInteger.ZERO, BigInteger.ZERO)
    val workloadPoints = WorkloadPointsEntity(null, generateCommunityTierPoints(), generateLicenseTierPoints(), generateCustodyTierPoints(), ZonedDateTime.now(), ZonedDateTime.now(), true, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, standardCourtReportWeighting, fastCourtReportWeighting)
    every { workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true) } returns t2aWorkloadPoints
    every { workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false) } returns workloadPoints
    val numberOfStandardCourtReports = 7
    val numberOfFastCourtReports = 12
    val standardCourtReports = (1..numberOfStandardCourtReports).map { CourtReport(CourtReportType.STANDARD) }
    val fastCourtReports = (1..numberOfFastCourtReports).map { CourtReport(CourtReportType.FAST) }

    val result = workloadCalculator.getWorkloadPoints(emptyList(), standardCourtReports + fastCourtReports)

    val expectedStandardCourtReportPoints = standardCourtReportWeighting.multiply(numberOfStandardCourtReports.toBigInteger())
    val expectedFastCourtReportPoints = fastCourtReportWeighting.multiply(numberOfFastCourtReports.toBigInteger())

    Assertions.assertEquals(expectedStandardCourtReportPoints.add(expectedFastCourtReportPoints), result)
  }

  private fun generateCommunityTierPoints() = CommunityTierPoints(BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN)
  private fun generateLicenseTierPoints() = LicenseTierPoints(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)
  private fun generateCustodyTierPoints() = CustodyTierPoints(BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO)
}
