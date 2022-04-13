package uk.gov.justice.digital.hmpps.hmppsworkload.integration.personManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import java.math.BigInteger
import java.util.UUID

class GetPersonManagerById : IntegrationTestBase() {

  @Test
  fun `can get person manager by Id`() {
    val storedPersonManager = PersonManagerEntity(crn = "CRN1", staffId = BigInteger.valueOf(123456789L), staffCode = "OM1", teamCode = "T1", offenderName = "John Doe", createdBy = "USER1", providerCode = "PV1")
    personManagerRepository.save(storedPersonManager)

    webTestClient.get()
      .uri("/allocation/person/${storedPersonManager.uuid}")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.id")
      .isEqualTo(storedPersonManager.uuid.toString())
      .jsonPath("$.staffId")
      .isEqualTo(storedPersonManager.staffId)
      .jsonPath("$.staffCode")
      .isEqualTo(storedPersonManager.staffCode)
      .jsonPath("$.teamCode")
      .isEqualTo(storedPersonManager.teamCode)
      .jsonPath("$.providerCode")
      .isEqualTo(storedPersonManager.providerCode)
      .jsonPath("$.createdBy")
      .isEqualTo(storedPersonManager.createdBy)
      .jsonPath("$.createdDate")
      .exists()
  }

  @Test
  fun `not found returned when getting person manager from uuid which does not exist`() {
    webTestClient.get()
      .uri("/allocation/person/${UUID.randomUUID()}")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isNotFound
  }
}
