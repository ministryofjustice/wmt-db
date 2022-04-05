package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

class GetCasesForOffenderManager : IntegrationTestBase() {

  @Test
  fun `get all cases allocated to offender manager`() {
    val staffCode = "OM1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$staffCode/cases")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Ben")
      .jsonPath("$.surname")
      .isEqualTo("Doe")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.code")
      .isEqualTo("OM1")
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.activeCases[0].crn")
      .isEqualTo("CRN2222")
      .jsonPath("$.activeCases[0].tier")
      .isEqualTo("B3")
      .jsonPath("$.activeCases[0].caseCategory")
      .isEqualTo("CUSTODY")
      .jsonPath("$.activeCases[1].crn")
      .isEqualTo("CRN3333")
      .jsonPath("$.activeCases[1].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[1].caseCategory")
      .isEqualTo("COMMUNITY")
      .jsonPath("$.activeCases[2].crn")
      .isEqualTo("CRN1111")
      .jsonPath("$.activeCases[2].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[2].caseCategory")
      .isEqualTo("LICENSE")
  }
}
