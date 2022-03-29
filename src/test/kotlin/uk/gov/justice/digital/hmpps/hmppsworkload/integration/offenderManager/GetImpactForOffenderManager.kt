package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.request.potentialCase

class GetImpactForOffenderManager : IntegrationTestBase() {

  @Test
  fun `can get impact for an offender manager`() {
    val staffId = 123456789L
    val crn = "CRN1"
    staffIdResponse(staffId)
    tierCalculationResponse(crn)
    singleActiveConvictionResponse(crn)
    // stub get active convictions
    webTestClient.post()
      .uri("/team/T1/offenderManagers/$staffId/impact")
      .bodyValue(potentialCase())
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("")
      .jsonPath("$.surname")
      .isEqualTo("")
      .jsonPath("$.grade")
      .isEqualTo("")
      .jsonPath("$.capacity")
      .isEqualTo(0)
      .jsonPath("$.code")
      .isEqualTo("")
      .jsonPath("$.potentialCapacity")
      .isEqualTo(0)
  }
}
