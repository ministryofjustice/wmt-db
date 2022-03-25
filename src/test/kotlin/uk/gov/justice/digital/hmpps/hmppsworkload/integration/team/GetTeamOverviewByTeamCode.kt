package uk.gov.justice.digital.hmpps.hmppsworkload.integration.team

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

class GetTeamOverviewByTeamCode : IntegrationTestBase() {

  @Test
  fun `can get team overview of offender managers by team code`() {
    val teamCode = "T1"
    teamStaffResponse(teamCode)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.offenderManagers[0].forename")
      .isEqualTo("Ben")
      .jsonPath("$.offenderManagers[0].surname")
      .isEqualTo("Doe")
      .jsonPath("$.offenderManagers[0].grade")
      .isEqualTo("PO")
      .jsonPath("$.offenderManagers[0].totalCommunityCases")
      .isEqualTo(15)
      .jsonPath("$.offenderManagers[0].totalCustodyCases")
      .isEqualTo(20)
      .jsonPath("$.offenderManagers[0].capacity")
      .isEqualTo(50)
      .jsonPath("$.offenderManagers[0].code")
      .isEqualTo("OM1")
      .jsonPath("$.offenderManagers[0].staffId")
      .isEqualTo(123456789)
      .jsonPath("$.offenderManagers[2].forename")
      .isEqualTo("Jane")
      .jsonPath("$.offenderManagers[2].surname")
      .isEqualTo("Doe")
      .jsonPath("$.offenderManagers[2].grade")
      .isEqualTo("DMY")
      .jsonPath("$.offenderManagers[2].totalCommunityCases")
      .isEqualTo(0)
      .jsonPath("$.offenderManagers[2].totalCustodyCases")
      .isEqualTo(0)
      .jsonPath("$.offenderManagers[2].capacity")
      .isEqualTo(0)
      .jsonPath("$.offenderManagers[2].code")
      .isEqualTo("OM3")
      .jsonPath("$.offenderManagers[2].staffId")
      .isEqualTo(987654321)
  }

  @Test
  fun `must return not found when team code is not matched`() {
    webTestClient.get()
      .uri("/team/RANDOMCODE/offenderManagers")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isNotFound
  }

  @Test
  fun `must return forbidden when auth token does not contain correct role`() {
    webTestClient.get()
      .uri("/team/T1/offenderManagers")
      .headers { it.authToken(roles = listOf("ROLE_RANDOM_ROLE")) }
      .exchange()
      .expectStatus()
      .isForbidden
  }
}
