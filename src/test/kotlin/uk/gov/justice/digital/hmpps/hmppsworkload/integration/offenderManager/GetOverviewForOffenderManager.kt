package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetOverviewForOffenderManager : IntegrationTestBase() {

  @Test
  fun `can get overview for an offender manager`() {
    val teamCode = "T1"
    val offenderManagerCode = "OM1"
    staffCodeResponse(offenderManagerCode, teamCode)
    val sentenceWithin30Days = SentenceEntity(BigInteger.TEN, "CRN3333", ZonedDateTime.now().minusMonths(2L), ZonedDateTime.now().plusDays(15L), "SP", null)
    sentenceRepository.save(sentenceWithin30Days)
    val sentenceAfter30Days = SentenceEntity(BigInteger.ONE, "CRN2222", ZonedDateTime.now().minusMonths(2L), ZonedDateTime.now().plusDays(45L), "SC", ZonedDateTime.now().plusDays(15L))
    sentenceRepository.save(sentenceAfter30Days)

    val team = teamRepository.findByCode(teamCode)

    val offenderManager = offenderManagerRepository.findByCode(offenderManagerCode)

    val secondWorkloadOwner = wmtWorkloadOwnerRepository.save(WMTWorkloadOwnerEntity(team = team, offenderManager = offenderManager, contractedHours = BigDecimal.valueOf(5)))

    val reduction = ReductionEntity(
      workloadOwnerId = secondWorkloadOwner.id!!, hours = BigDecimal.valueOf(5),
      effectiveFrom = LocalDate.now().minusDays(2).atStartOfDay(
        ZoneId.systemDefault()
      ),
      effectiveTo = LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()), status = ReductionStatus.ACTIVE, reductionReasonId = 1
    )
    reductionsRepository.save(reduction)

    reductionsRepository.save(
      ReductionEntity(
        workloadOwnerId = secondWorkloadOwner.id!!, hours = BigDecimal.valueOf(5),
        effectiveFrom = LocalDate.now().minusDays(2).atStartOfDay(
          ZoneId.systemDefault()
        ),
        effectiveTo = LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()), status = ReductionStatus.DELETED, reductionReasonId = 1
      )
    )

    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$offenderManagerCode")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
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
      .isEqualTo(secondWorkloadOwner.contractedHours)
      .jsonPath("$.totalReductionHours")
      .isEqualTo(reduction.hours)
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
        reduction.effectiveTo.withZoneSameInstant(ZoneOffset.UTC).format(
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
      .jsonPath("$.paroleReportsDue")
      .isEqualTo(5)
      .jsonPath("$.caseEndDue")
      .isEqualTo(1)
      .jsonPath("$.releasesDue")
      .isEqualTo(1)
  }

  @Test
  fun `can get overview for an offender manager without any reductions`() {
    val teamCode = "T1"
    val offenderManagerCode = "OM2"
    staffCodeResponse(offenderManagerCode, teamCode)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$offenderManagerCode")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
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

  @Test
  fun `can get overview for an offender manager without workload`() {
    val teamCode = "T1"
    val offenderManagerCode = "NOWORKLOAD1"
    staffCodeResponse(offenderManagerCode, teamCode)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$offenderManagerCode")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.capacity")
      .isEqualTo(0)
      .jsonPath("$.code")
      .isEqualTo(offenderManagerCode)
      .jsonPath("$.teamName")
      .isEqualTo("Test Team")
      .jsonPath("$.totalCases")
      .isEqualTo(0)
      .jsonPath("$.weeklyHours")
      .isEqualTo(37)
      .jsonPath("$.totalReductionHours")
      .isEqualTo(0)
      .jsonPath("$.pointsAvailable")
      .isEqualTo(2176)
      .jsonPath("$.pointsUsed")
      .isEqualTo(0)
      .jsonPath("$.pointsRemaining")
      .isEqualTo(2176)
      .jsonPath("$.nextReductionChange")
      .doesNotExist()
      .jsonPath("$.caseTotals.a")
      .isEqualTo(0)
      .jsonPath("$.caseTotals.b")
      .isEqualTo(0)
      .jsonPath("$.caseTotals.c")
      .isEqualTo(0)
      .jsonPath("$.caseTotals.d")
      .isEqualTo(0)
      .jsonPath("$.caseTotals.untiered")
      .isEqualTo(0)
  }
}
