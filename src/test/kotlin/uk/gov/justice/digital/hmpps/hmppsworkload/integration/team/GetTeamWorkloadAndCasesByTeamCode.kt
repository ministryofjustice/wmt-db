package uk.gov.justice.digital.hmpps.hmppsworkload.integration.team

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

@Disabled
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
      .jsonPath("$.teams[0].teamCode")
      .isEqualTo("T1")
      .jsonPath("$.teams[0].workload")
      .isEqualTo("88%")
      .jsonPath("$.teams[0].cases")
      .isEqualTo("22")
  }

  @Test
  fun `must return not found when team code is not matched`() {
    webTestClient.get()
      .uri("/team/workloadcases?teams=teamCodeNotFound")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isNotFound
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
