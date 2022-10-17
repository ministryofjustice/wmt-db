package uk.gov.justice.digital.hmpps.hmppsworkload.integration.caseload

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.CaseCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WMTCaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WMTWorkloadEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WorkloadReportEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.OffenderManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PduEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RegionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.TeamEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetCombinedCaseload
import java.math.BigDecimal
import java.math.BigInteger

class GetCombinedCaseloadTest : IntegrationTestBase() {

  private lateinit var getCaseLoad: GetCombinedCaseload

  @BeforeAll
  fun setup() {
    getCaseLoad = GetCombinedCaseload(offenderManagerRepository, personManagerRepository, caseDetailsRepository)
  }

  private fun setupWmtOffenderManager(staffCode: String, teamCode: String, tier: String, crn: String, caseType: CaseType) {
    val region = regionRepository.save(RegionEntity(code = "REGION1", description = "Region 1"))
    val pdu = pduRepository.save(PduEntity(code = "LDU1", description = "Local Delivery Unit (Actually a Probation Delivery Unit)", region = region))
    val team = teamRepository.save(TeamEntity(code = teamCode, description = "Team 1", ldu = pdu))
    val offenderManager = offenderManagerRepository.save(OffenderManagerEntity(code = staffCode, forename = "Jane", surname = "Doe", typeId = 1))
    val workloadOwner = wmtWorkloadOwnerRepository.save(WMTWorkloadOwnerEntity(offenderManager = offenderManager, team = team, contractedHours = BigDecimal.valueOf(37.5)))
    val workloadReport = workloadReportRepository.save((WorkloadReportEntity()))
    val wmtWorkload = wmtWorkloadRepository.save(WMTWorkloadEntity(workloadOwner = workloadOwner, workloadReport = workloadReport))
    val tierCategory = caseCategoryRepository.save(CaseCategoryEntity(categoryId = 1, categoryName = tier))
    wmtCaseDetailsRepository.save(WMTCaseDetailsEntity(workload = wmtWorkload, crn = crn, tierCategory = tierCategory, caseType = caseType, teamCode = teamCode))
  }

  @Test
  fun `must return list of wmt cases`() {
    val staffCode = "OM1"
    val teamCode = "T1"
    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN1111")
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type, "Jane", "Doe"))

    setupWmtOffenderManager(staffCode, teamCode, realtimeCase.tier.name, realtimeCase.crn, realtimeCase.type)

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, actualCases[0])
  }

  @Test
  fun `must not return list of cases if no realtime data exist`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    setupWmtOffenderManager(staffCode, teamCode, Tier.C2.name, "CRN12345", CaseType.COMMUNITY)

    Assertions.assertEquals(0, getCaseLoad.getCases(staffCode, teamCode).size)
  }

  @Test
  fun `must return list of person manager cases when realtime data exists`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN1112")

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode", isActive = true
      )
    )

    // realtime
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, CaseType.LICENSE, "Jane", "Doe"))

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase.crn, actualCases[0].crn)
  }

  @Test
  fun `must return one case when offender manager and person manager have entries for the same CRN`() {

    val staffCode = "OM1"
    val teamCode = "T1"

    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN1111")
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type, "Jane", "Doe"))

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode", isActive = true
      )
    )

    setupWmtOffenderManager(staffCode, teamCode, realtimeCase.tier.name, realtimeCase.crn, realtimeCase.type)

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(1, actualCases.size)
  }
  @Test
  fun `must not be in caseload if allocated to another staff member`() {

    val originalStaffCode = "STAFF1"
    val teamCode = "TEAM!"
    val newStaffCode = "STAFF2"

    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN9191")
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, CaseType.LICENSE, "Jane", "Doe"))

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = originalStaffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode", isActive = false
      )
    )

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = newStaffCode,
        teamCode = teamCode, staffId = BigInteger.ONE, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode", isActive = true
      )
    )

    val actualCases = getCaseLoad.getCases(originalStaffCode, teamCode)

    Assertions.assertEquals(0, actualCases.size)
  }
}
