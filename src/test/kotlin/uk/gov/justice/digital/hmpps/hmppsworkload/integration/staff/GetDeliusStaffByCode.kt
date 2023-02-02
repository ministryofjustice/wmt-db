package uk.gov.justice.digital.hmpps.hmppsworkload.integration.staff

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi

class GetDeliusStaffByCode : IntegrationTestBase() {

  @Test
  fun `get staff by Code`() {
    val staffCode = "Staff01"
    communityApi.staffCodeResponse(staffCode, "OM1", "T1")
    webTestClient.get()
      .uri("/staff/code/$staffCode")
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
      .isEqualTo("sheila.hancock@test.justice.gov.uk")
      .jsonPath("$.id")
      .isEqualTo(123456)
  }

  @Test
  fun `not found returned when no staff exists for Code`() {
    val staffCode = "Staff01"
    webTestClient.get()
      .uri("/staff/code/$staffCode")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isNotFound
  }
}
