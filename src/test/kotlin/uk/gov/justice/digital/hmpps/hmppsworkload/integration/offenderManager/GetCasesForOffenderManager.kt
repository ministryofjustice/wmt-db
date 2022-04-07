package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import java.util.Arrays.asList

class GetCasesForOffenderManager : IntegrationTestBase() {

  @Test
  fun `get all cases allocated to offender manager`() {
    val staffCode = "OM1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode)
    offenderSearchByCrnsResponse(asList("CRN2222", "CRN3333", "CRN1111"))
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
      .isEqualTo(staffCode)
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.activeCases[0].crn")
      .isEqualTo("CRN2222")
      .jsonPath("$.activeCases[0].tier")
      .isEqualTo("B3")
      .jsonPath("$.activeCases[0].caseCategory")
      .isEqualTo("CUSTODY")
      .jsonPath("$.activeCases[0].forename")
      .isEqualTo("Sally")
      .jsonPath("$.activeCases[0].surname")
      .isEqualTo("Smith")
      .jsonPath("$.activeCases[1].crn")
      .isEqualTo("CRN3333")
      .jsonPath("$.activeCases[1].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[1].caseCategory")
      .isEqualTo("COMMUNITY")
      .jsonPath("$.activeCases[1].forename")
      .isEqualTo("John")
      .jsonPath("$.activeCases[1].surname")
      .isEqualTo("Williams")
      .jsonPath("$.activeCases[2].crn")
      .isEqualTo("CRN1111")
      .jsonPath("$.activeCases[2].tier")
      .isEqualTo("C1")
      .jsonPath("$.activeCases[2].caseCategory")
      .isEqualTo("LICENSE")
      .jsonPath("$.activeCases[2].forename")
      .isEqualTo("John")
      .jsonPath("$.activeCases[2].surname")
      .isEqualTo("Doe")
  }

  @Test
  fun `Get staff member without any WMT active cases`() {
    val staffCode = "NOWORKLOAD1"
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
      .isEqualTo(staffCode)
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.activeCases")
      .isEmpty
  }

  @Test
  fun `still return response if not all offender details are returned`() {
    val staffCode = "OM1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode)
    oneOffenderReturnedWhenSearchByCrnsResponse(asList("CRN2222", "CRN3333", "CRN1111"))
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
      .isEqualTo(staffCode)
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
      .jsonPath("$.activeCases[2].forename")
      .isEqualTo("John")
      .jsonPath("$.activeCases[2].surname")
      .isEqualTo("Doe")
  }
}
