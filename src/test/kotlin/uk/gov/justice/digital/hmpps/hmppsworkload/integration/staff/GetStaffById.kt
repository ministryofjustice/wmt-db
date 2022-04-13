package uk.gov.justice.digital.hmpps.hmppsworkload.integration.staff

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

class GetStaffById : IntegrationTestBase() {

  @Test
  fun `get staff by ID`() {
    val staffId = 123456789L
    staffIdResponse(staffId, "OM1", "T1")
    webTestClient.get()
      .uri("/staff/$staffId")
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
      .jsonPath("$.email")
      .isEqualTo("ben.doe@test.justice.gov.uk")
      .jsonPath("$.id")
      .isEqualTo(staffId)
  }
}
