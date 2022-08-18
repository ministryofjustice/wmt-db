package uk.gov.justice.digital.hmpps.hmppsworkload.integration.caseload

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetCombinedCaseload
import java.math.BigInteger

class GetCombinedCaseloadTest : IntegrationTestBase() {

  private lateinit var getCaseLoad: GetCombinedCaseload

  @BeforeAll
  fun setup() {
    getCaseLoad = GetCombinedCaseload(offenderManagerRepository, personManagerRepository, caseDetailsRepository)
  }

  @Test
  fun `must return list of wmt cases`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN1111")
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, CaseType.LICENSE))

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, actualCases[0])
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

    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN1112")

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    // realtime
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, CaseType.LICENSE))

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase.crn, actualCases[0].crn)
  }

  @Test
  fun `must return one case when offender manager and person manager have entries for the same CRN`() {

    val staffCode = "OM1"
    val teamCode = "T1"

    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, "CRN1111")
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, CaseType.LICENSE))

    personManagerRepository.save(
      PersonManagerEntity(
        crn = realtimeCase.crn, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    val actualCases = getCaseLoad.getCases(staffCode, teamCode)

    Assertions.assertEquals(1, actualCases.size)
  }

  @Test
  fun `must return latest case allocated when last allocated case requested`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val savedEntity = personManagerRepository.save(
      PersonManagerEntity(
        crn = "CRN6634", staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val personManagerEntity = personManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, personManagerEntity.crn, personManagerEntity.createdDate)
    // realtime
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type))
    val result = getCaseLoad.getLastAllocatedCase(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, result)
  }

  @Test
  fun `must only return when case details are known`() {
    val staffCode = "OM1"
    val teamCode = "T1"
    personManagerRepository.save(
      PersonManagerEntity(
        crn = "CRN6634", staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    Assertions.assertNull(getCaseLoad.getLastAllocatedCase(staffCode, teamCode))
  }

  @Test
  fun `must return latest case when multiple are allocated`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val personManagerEntity = personManagerRepository.save(
      PersonManagerEntity(
        crn = "CRN6634", staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    caseDetailsRepository.save(CaseDetailsEntity(personManagerEntity.crn, Tier.A1, CaseType.LICENSE))

    val savedEntity = personManagerRepository.save(
      PersonManagerEntity(
        crn = "CRN9977", staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val latestPersonManagerEntity = personManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = Case(Tier.C3, CaseType.COMMUNITY, false, latestPersonManagerEntity.crn, latestPersonManagerEntity.createdDate)
    // realtime
    caseDetailsRepository.save(CaseDetailsEntity(latestPersonManagerEntity.crn, realtimeCase.tier, realtimeCase.type))

    val result = getCaseLoad.getLastAllocatedCase(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, result)
  }

  @Test
  fun `must return latest case which has case details`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val savedEntity = personManagerRepository.save(
      PersonManagerEntity(
        crn = "CRN6634", staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val personManagerEntity = personManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = Case(Tier.A1, CaseType.LICENSE, false, personManagerEntity.crn, personManagerEntity.createdDate)
    caseDetailsRepository.save(CaseDetailsEntity(personManagerEntity.crn, realtimeCase.tier, realtimeCase.type))

    personManagerRepository.save(
      PersonManagerEntity(
        crn = "CRN9977", staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, offenderName = "offenderName", createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    val result = getCaseLoad.getLastAllocatedCase(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, result)
  }
}
