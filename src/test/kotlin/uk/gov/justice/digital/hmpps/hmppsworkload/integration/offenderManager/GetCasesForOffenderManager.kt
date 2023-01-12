package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity

class GetCasesForOffenderManager : IntegrationTestBase() {

  val teamCode = "T1"
  val staffCodeOM = "OM1"
  val staffCodeNO = "NOWORKLOAD1"

  @Test
  fun `get all cases allocated to offender manager`() {
    staffCodeResponse(staffCodeOM, teamCode)
    val realTimeCaseDetails = caseDetailsRepository.saveAll(listOf(CaseDetailsEntity("CRN2222", Tier.B3, CaseType.CUSTODY, "Sally", "Smith"), CaseDetailsEntity("CRN3333", Tier.C1, CaseType.COMMUNITY, "John", "Williams"), CaseDetailsEntity("CRN1111", Tier.C1, CaseType.LICENSE, "John", "Doe")))
    val wmtStaff = setupCurrentWmtStaff(staffCodeOM, teamCode)

    realTimeCaseDetails.forEach { caseDetails ->
      setupWmtManagedCase(wmtStaff, caseDetails.tier, caseDetails.crn, caseDetails.type)
    }

    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$staffCodeOM/cases")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.code")
      .isEqualTo(staffCodeOM)
      .jsonPath("$.email")
      .isEqualTo("sheila.hancock@test.justice.gov.uk")
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.activeCases[?(@.crn == 'CRN2222')].tier")
      .isEqualTo("B3")
      .jsonPath("$.activeCases[?(@.crn == 'CRN2222')].caseCategory")
      .isEqualTo("CUSTODY")
      .jsonPath("$.activeCases[?(@.crn == 'CRN2222')].forename")
      .isEqualTo("Sally")
      .jsonPath("$.activeCases[?(@.crn == 'CRN2222')].surname")
      .isEqualTo("Smith")
      .jsonPath("$.activeCases[?(@.crn == 'CRN3333')].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[?(@.crn == 'CRN3333')].caseCategory")
      .isEqualTo("COMMUNITY")
      .jsonPath("$.activeCases[?(@.crn == 'CRN3333')].forename")
      .isEqualTo("John")
      .jsonPath("$.activeCases[?(@.crn == 'CRN3333')].surname")
      .isEqualTo("Williams")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].caseCategory")
      .isEqualTo("LICENSE")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].forename")
      .isEqualTo("John")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].surname")
      .isEqualTo("Doe")
  }

  @Test
  fun `Get staff member without any email`() {
    staffCodeResponse(staffCodeNO, teamCode, email = null)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$staffCodeNO/cases")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.email")
      .doesNotExist()
  }

  @Test
  fun `Get staff member without any WMT active cases`() {
    staffCodeResponse(staffCodeNO, teamCode)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$staffCodeNO/cases")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.code")
      .isEqualTo(staffCodeNO)
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.activeCases")
      .isEmpty
  }

  @Test
  fun `still return response if not all offender details are returned`() {
    staffCodeResponse(staffCodeOM, teamCode)
    val caseDetails = caseDetailsRepository.save(CaseDetailsEntity("CRN1111", Tier.C1, CaseType.LICENSE, "John", "Doe"))
    val wmtStaff = setupCurrentWmtStaff(staffCodeOM, teamCode)
    setupWmtManagedCase(wmtStaff, caseDetails.tier, caseDetails.crn, caseDetails.type)
    setupWmtManagedCase(wmtStaff, Tier.B3, "CRN2222", CaseType.CUSTODY)
    setupWmtManagedCase(wmtStaff, Tier.C1, "CRN3333", CaseType.COMMUNITY)

    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$staffCodeOM/cases")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.code")
      .isEqualTo(staffCodeOM)
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.activeCases[?(@.crn == 'CRN2222')].tier")
      .isEqualTo("B3")
      .jsonPath("$.activeCases[?(@.crn == 'CRN2222')].caseCategory")
      .isEqualTo("CUSTODY")
      .jsonPath("$.activeCases[?(@.crn == 'CRN3333')].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[?(@.crn == 'CRN3333')].caseCategory")
      .isEqualTo("COMMUNITY")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].caseCategory")
      .isEqualTo("LICENSE")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].forename")
      .isEqualTo("John")
      .jsonPath("$.activeCases[?(@.crn == 'CRN1111')].surname")
      .isEqualTo("Doe")
  }
}
