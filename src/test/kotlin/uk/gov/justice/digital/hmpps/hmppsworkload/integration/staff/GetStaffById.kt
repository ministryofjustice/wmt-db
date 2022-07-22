package uk.gov.justice.digital.hmpps.hmppsworkload.integration.staff

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import java.math.BigInteger

class GetStaffById : IntegrationTestBase() {

  @Test
  fun `get staff by ID`() {
    val staffId = BigInteger.valueOf(123456789L)
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

  @Test
  fun `not found returned when no staff exists for ID`() {
    val staffId = BigInteger.valueOf(123456789L)
    webTestClient.get()
      .uri("/staff/$staffId")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isNotFound
  }
}
