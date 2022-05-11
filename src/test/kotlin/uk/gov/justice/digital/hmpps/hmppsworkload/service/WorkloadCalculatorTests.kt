package uk.gov.justice.digital.hmpps.hmppsworkload.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AssessmentType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReportType
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

  @BeforeEach
  fun setup() {
    mockWorkloadPoints(isT2A = true)
  }

  @Test
  fun `must sum all cases depending on their tier, category and whether they are T2A or not`() {
    val t2aWorkloadPoints = mockWorkloadPoints(isT2A = true)
    val workloadPoints = mockWorkloadPoints(isT2A = false)
    val numberOfT2aCases = 10
    val numberOfCases = 5
    val t2aCases = (1..numberOfT2aCases).map { Case(Tier.B2, CaseType.COMMUNITY, true, "CRNCOM$it") }
    val cases = (1..numberOfCases).map { Case(Tier.C1, CaseType.CUSTODY, false, "CRNCUST$it") }
    val result = workloadCalculator.getWorkloadPoints(t2aCases + cases, emptyList(), emptyList(), emptyList())
    val t2aExpectedWorkloadPoints = t2aWorkloadPoints.communityTierPoints.B2Points.multiply(numberOfT2aCases.toBigInteger())
    val casesExpectedWorkloadPoints = workloadPoints.custodyTierPoints.C1Points.multiply(numberOfCases.toBigInteger())
    Assertions.assertEquals(t2aExpectedWorkloadPoints.add(casesExpectedWorkloadPoints), result)
  }

  @Test
  fun `must sum all court reports`() {
    val standardCourtReportWeighting = BigInteger.TEN
    val fastCourtReportWeighting = BigInteger.TWO
    mockWorkloadPoints(standardCourtReportWeighting = standardCourtReportWeighting, fastCourtReportWeighting = fastCourtReportWeighting, isT2A = false)
    val numberOfStandardCourtReports = 7
    val numberOfFastCourtReports = 12
    val standardCourtReports = (1..numberOfStandardCourtReports).map { CourtReport(CourtReportType.STANDARD) }
    val fastCourtReports = (1..numberOfFastCourtReports).map { CourtReport(CourtReportType.FAST) }

    val result = workloadCalculator.getWorkloadPoints(emptyList(), standardCourtReports + fastCourtReports, emptyList(), emptyList())

    val expectedStandardCourtReportPoints = standardCourtReportWeighting.multiply(numberOfStandardCourtReports.toBigInteger())
    val expectedFastCourtReportPoints = fastCourtReportWeighting.multiply(numberOfFastCourtReports.toBigInteger())

    Assertions.assertEquals(expectedStandardCourtReportPoints.add(expectedFastCourtReportPoints), result)
  }

  @Test
  fun `must sum all institutional reports which require a parole report`() {
    val paroleReportWeighting = BigInteger.TEN
    val paroleEnabled = true
    mockWorkloadPoints(isT2A = false, paroleReportWeighting = paroleReportWeighting, paroleEnabled = paroleEnabled)

    val numberOfParoleReports = 15
    val numberOfOtherReports = 6

    val paroleReports = (1..numberOfParoleReports).map { InstitutionalReport(InstitutionalReportType.PAROLE_REPORT) }
    val otherReports = (1..numberOfOtherReports).map { InstitutionalReport(InstitutionalReportType.OTHER) }

    val result = workloadCalculator.getWorkloadPoints(emptyList(), emptyList(), paroleReports + otherReports, emptyList())

    Assertions.assertEquals(paroleReportWeighting.multiply(numberOfParoleReports.toBigInteger()), result)
  }

  @Test
  fun `total of institutional reports must be zero when not enabled`() {
    val paroleReportWeighting = BigInteger.TEN
    val paroleEnabled = false
    mockWorkloadPoints(isT2A = false, paroleReportWeighting = paroleReportWeighting, paroleEnabled = paroleEnabled)

    val numberOfParoleReports = 15
    val numberOfOtherReports = 6

    val paroleReports = (1..numberOfParoleReports).map { InstitutionalReport(InstitutionalReportType.PAROLE_REPORT) }
    val otherReports = (1..numberOfOtherReports).map { InstitutionalReport(InstitutionalReportType.OTHER) }

    val result = workloadCalculator.getWorkloadPoints(emptyList(), emptyList(), paroleReports + otherReports, emptyList())

    Assertions.assertEquals(BigInteger.ZERO, result)
  }

  @Test
  fun `must sum all ARM assessments`() {
    val licenseARMAssessmentWeighting = BigInteger.TWO
    val communityARMAssessmentWeighting = BigInteger.TEN

    mockWorkloadPoints(isT2A = false, licenseARMAssessmentWeighting = licenseARMAssessmentWeighting, communityARMAssessmentWeighting = communityARMAssessmentWeighting)

    val numberOfLicenseArmAssessments = 12
    val numberOfCommunityArmAssessments = 8
    val numberOfOtherArmAssessments = 20

    val licenseArmAssessments = (1..numberOfLicenseArmAssessments).map { Assessment(AssessmentType.ARMS, CaseType.LICENSE) }
    val communityArmAssessments = (1..numberOfCommunityArmAssessments).map { Assessment(AssessmentType.ARMS, CaseType.COMMUNITY) }
    val otherAssessments = (1..numberOfOtherArmAssessments).map { Assessment(AssessmentType.OTHER, CaseType.UNKNOWN) }

    val result = workloadCalculator.getWorkloadPoints(emptyList(), emptyList(), emptyList(), licenseArmAssessments + communityArmAssessments + otherAssessments)

    val expectedLicensePointsTotal = licenseARMAssessmentWeighting.multiply(numberOfLicenseArmAssessments.toBigInteger())
    val expectedCommunityPointsTotal = communityARMAssessmentWeighting.multiply(numberOfCommunityArmAssessments.toBigInteger())

    Assertions.assertEquals(expectedLicensePointsTotal.add(expectedCommunityPointsTotal), result)
  }

  private fun mockWorkloadPoints(
    communityTierPoints: CommunityTierPoints = generateCommunityTierPoints(),
    licenseTierPoints: LicenseTierPoints = generateLicenseTierPoints(),
    custodyTierPoints: CustodyTierPoints = generateCustodyTierPoints(),
    standardCourtReportWeighting: BigInteger = BigInteger.ZERO,
    fastCourtReportWeighting: BigInteger = BigInteger.ZERO,
    isT2A: Boolean = false,
    paroleReportWeighting: BigInteger = BigInteger.ZERO,
    paroleEnabled: Boolean = true,
    licenseARMAssessmentWeighting: BigInteger = BigInteger.ZERO,
    communityARMAssessmentWeighting: BigInteger = BigInteger.ZERO
  ): WorkloadPointsEntity {
    val workloadPoints = WorkloadPointsEntity(
      null,
      communityTierPoints,
      licenseTierPoints,
      custodyTierPoints,
      ZonedDateTime.now(),
      ZonedDateTime.now(),
      isT2A,
      BigDecimal.ZERO,
      BigDecimal.ZERO,
      BigDecimal.ZERO,
      BigDecimal.ZERO,
      BigDecimal.ZERO,
      standardCourtReportWeighting,
      fastCourtReportWeighting,
      paroleReportWeighting,
      paroleEnabled,
      licenseARMAssessmentWeighting,
      communityARMAssessmentWeighting
    )
    every { workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(isT2A) } returns workloadPoints
    return workloadPoints
  }

  private fun generateCommunityTierPoints() = CommunityTierPoints(BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN)
  private fun generateLicenseTierPoints() = LicenseTierPoints(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)
  private fun generateCustodyTierPoints() = CustodyTierPoints(BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO, BigInteger.TWO)
}
