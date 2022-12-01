package uk.gov.justice.digital.hmpps.hmppsworkload.integration.eventManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import java.util.UUID

class GetEventManagerById : IntegrationTestBase() {

  @Test
  fun `not found returned when getting event manager from uuid which does not exist`() {
    webTestClient.get()
      .uri("/allocation/event/${UUID.randomUUID()}")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isNotFound
  }
}
