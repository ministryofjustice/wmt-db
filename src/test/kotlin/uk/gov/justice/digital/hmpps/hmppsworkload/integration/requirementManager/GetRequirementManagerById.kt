package uk.gov.justice.digital.hmpps.hmppsworkload.integration.requirementManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.math.BigInteger
import java.util.UUID

class GetRequirementManagerById : IntegrationTestBase() {

  @Test
  fun `can get requirement manager by Id`() {
    val storedRequirementManager = RequirementManagerEntity(crn = "CRN1", staffId = BigInteger.valueOf(123456789L), staffCode = "OM1", teamCode = "T1", createdBy = "USER1", providerCode = "PV1", eventId = BigInteger.valueOf(567891234L), requirementId = BigInteger.valueOf(24680L))
    requirementManagerRepository.save(storedRequirementManager)

    webTestClient.get()
      .uri("/allocation/requirements/${storedRequirementManager.uuid}")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.id")
      .isEqualTo(storedRequirementManager.uuid.toString())
      .jsonPath("$.staffId")
      .isEqualTo(storedRequirementManager.staffId)
      .jsonPath("$.staffCode")
      .isEqualTo(storedRequirementManager.staffCode)
      .jsonPath("$.teamCode")
      .isEqualTo(storedRequirementManager.teamCode)
      .jsonPath("$.providerCode")
      .isEqualTo(storedRequirementManager.providerCode)
      .jsonPath("$.createdBy")
      .isEqualTo(storedRequirementManager.createdBy)
      .jsonPath("$.createdDate")
      .exists()
      .jsonPath("$.eventId")
      .isEqualTo(storedRequirementManager.eventId)
      .jsonPath("$.requirementId")
      .isEqualTo(storedRequirementManager.requirementId)
  }

  @Test
  fun `not found returned when getting requirement manager from uuid which does not exist`() {
    webTestClient.get()
      .uri("/allocation/requirements/${UUID.randomUUID()}")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isNotFound
  }
}
