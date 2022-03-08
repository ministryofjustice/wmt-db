package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.request.potentialCase

class GetPotentialCapacityForOffenderManager : IntegrationTestBase() {

  @Test
  fun `can get potential capacity for an offender manager`() {
    webTestClient.post()
      .uri("/team/T1/offenderManagers/OM1/potentialCases")
      .bodyValue(potentialCase())
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
        it.contentType = MediaType.APPLICATION_JSON
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
      .jsonPath("$.capacity")
      .isEqualTo(50)
      .jsonPath("$.code")
      .isEqualTo("OM1")
      .jsonPath("$.potentialCapacity")
      .isEqualTo(61.5)
  }
}
