package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetOverviewForOffenderManager : IntegrationTestBase() {

  @Test
  fun `can get overview for an offender manager`() {
    webTestClient.get()
      .uri("/team/T1/offenderManagers/OM1")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
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
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.totalCases")
      .isEqualTo(35)
      .jsonPath("$.weeklyHours")
      .isEqualTo(37)
      .jsonPath("$.totalReductionHours")
      .isEqualTo(10)
      .jsonPath("$.pointsAvailable")
      .isEqualTo(1000)
      .jsonPath("$.pointsUsed")
      .isEqualTo(500)
      .jsonPath("$.pointsRemaining")
      .isEqualTo(500)
      .jsonPath("$.lastUpdatedOn")
      .isEqualTo("2013-11-03T09:00:00")
      .jsonPath("$.nextReductionChange")
      .isEqualTo(
        ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT), ZoneId.systemDefault()).plusHours(1).plusDays(2).format(
          DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
      )
      .jsonPath("$.caseTotals.a")
      .isEqualTo(6)
      .jsonPath("$.caseTotals.b")
      .isEqualTo(6)
      .jsonPath("$.caseTotals.c")
      .isEqualTo(6)
      .jsonPath("$.caseTotals.d")
      .isEqualTo(6)
      .jsonPath("$.caseTotals.untiered")
      .isEqualTo(6)
  }

  @Test
  fun `can get overview for an offender manager without any reductions`() {
    webTestClient.get()
      .uri("/team/T1/offenderManagers/OM2")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_READ"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.totalReductionHours")
      .isEqualTo(0)
      .jsonPath("$.nextReductionChange")
      .doesNotExist()
  }
}
