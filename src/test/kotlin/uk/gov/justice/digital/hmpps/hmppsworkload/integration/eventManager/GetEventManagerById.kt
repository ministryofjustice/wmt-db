package uk.gov.justice.digital.hmpps.hmppsworkload.integration.eventManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.math.BigInteger
import java.util.UUID

class GetEventManagerById : IntegrationTestBase() {

  @Test
  fun `can get event manager by Id`() {
    val storedEventManager = EventManagerEntity(crn = "CRN1", staffId = BigInteger.valueOf(123456789L), staffCode = "OM1", teamCode = "T1", createdBy = "USER1", providerCode = "PV1", eventId = BigInteger.valueOf(567891234L), isActive = true)
    eventManagerRepository.save(storedEventManager)

    webTestClient.get()
      .uri("/allocation/event/${storedEventManager.uuid}")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.id")
      .isEqualTo(storedEventManager.uuid.toString())
      .jsonPath("$.staffId")
      .isEqualTo(storedEventManager.staffId)
      .jsonPath("$.staffCode")
      .isEqualTo(storedEventManager.staffCode)
      .jsonPath("$.teamCode")
      .isEqualTo(storedEventManager.teamCode)
      .jsonPath("$.providerCode")
      .isEqualTo(storedEventManager.providerCode)
      .jsonPath("$.createdBy")
      .isEqualTo(storedEventManager.createdBy)
      .jsonPath("$.createdDate")
      .exists()
      .jsonPath("$.eventId")
      .isEqualTo(storedEventManager.eventId)
  }

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
