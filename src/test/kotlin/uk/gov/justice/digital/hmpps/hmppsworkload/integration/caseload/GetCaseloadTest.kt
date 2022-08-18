package uk.gov.justice.digital.hmpps.hmppsworkload.integration.caseload

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType.LICENSE
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier.A1
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetCaseload
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetCurrentlyManagedCaseload
import java.math.BigInteger

class GetCaseloadTest : IntegrationTestBase() {

  private lateinit var getCaseLoad: GetCaseload
  @BeforeAll
  fun setup() {
    getCaseLoad = GetCurrentlyManagedCaseload(personManagerRepository, caseDetailsRepository)
  }
  @Test
  fun `must not return list of cases if no realtime data exist`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    Assertions.assertEquals(0, getCaseLoad.getCases(staffCode, teamCode).size)
  }

  @Test
  fun `must return list of person manager cases when realtime data exists`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val realtimeCase = Case(A1, LICENSE, false, "CRN1112")

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type))

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase.crn, actualCases[0].crn)
  }
}
