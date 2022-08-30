package uk.gov.justice.digital.hmpps.hmppsworkload.integration.team

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

class GetTeamWorkloadAndCasesByTeamCode : IntegrationTestBase() {
  val teamCode = "T1"
  @Test
  fun `can get workload and cases by team code`() {

    webTestClient.get()
      .uri("/team/workloadcases?teams=$teamCode")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.[0].teamCode")
      .isEqualTo("T1")
      .jsonPath("$.[0].workload")
      .isEqualTo("74.0")
      .jsonPath("$.[0].totalCases")
      .isEqualTo("2")
  }

  @Test
  fun `must return forbidden when auth token does not contain correct role`() {
    webTestClient.get()
      .uri("/team/workloadcases?teams=$teamCode")
      .headers { it.authToken(roles = listOf("ROLE_RANDOM_ROLE")) }
      .exchange()
      .expectStatus()
      .isForbidden
  }
}
