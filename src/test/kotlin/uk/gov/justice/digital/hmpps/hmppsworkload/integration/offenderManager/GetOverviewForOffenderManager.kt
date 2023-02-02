package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionReasonEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.TiersEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WMTWorkloadEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GetOverviewForOffenderManager : IntegrationTestBase() {

  private fun setupWmtTierTotal(tier: Tier?, workload: WMTWorkloadEntity, total: Int): TiersEntity {
    val wmtTier = if (tier != null) setupWmtCaseCategoryTier(tier) else setupWmtUntiered()
    return tiersRepository.save(TiersEntity(workload = workload, caseType = CaseType.CUSTODY, tierCategory = wmtTier, totalFilteredCases = total))
  }
  @Test
  fun `can get overview for an offender manager`() {
    val teamCode = "T1"
    val offenderManagerCode = "OM1"
    communityApi.staffCodeResponse(offenderManagerCode, teamCode)
    val wmtStaff = setupCurrentWmtStaff(offenderManagerCode, teamCode)
    val sentenceWithin30Days = SentenceEntity(BigInteger.TEN, "CRN3333", ZonedDateTime.now().minusMonths(2L), ZonedDateTime.now().plusDays(15L), "SP", null)
    sentenceRepository.save(sentenceWithin30Days)
    val sentenceAfter30Days = SentenceEntity(BigInteger.ONE, "CRN2222", ZonedDateTime.now().minusMonths(2L), ZonedDateTime.now().plusDays(45L), "SC", ZonedDateTime.now().plusDays(15L))
    sentenceRepository.save(sentenceAfter30Days)

    setupWmtManagedCase(wmtStaff, Tier.A2, sentenceWithin30Days.crn, CaseType.COMMUNITY)
    setupWmtManagedCase(wmtStaff, Tier.D2, sentenceAfter30Days.crn, CaseType.CUSTODY)

    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    val reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))
    val reduction = ReductionEntity(
      workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(5),
      effectiveFrom = LocalDate.now().minusDays(2).atStartOfDay(
        ZoneId.systemDefault()
      ),
      effectiveTo = LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!
    )
    reductionsRepository.save(reduction)

    reductionsRepository.save(
      ReductionEntity(
        workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(5),
        effectiveFrom = LocalDate.now().minusDays(2).atStartOfDay(
          ZoneId.systemDefault()
        ),
        effectiveTo = LocalDate.now().plusDays(2).atStartOfDay(ZoneId.systemDefault()), status = ReductionStatus.DELETED, reductionReasonId = reductionReason.id!!
      )
    )

    val aTierTotal = setupWmtTierTotal(Tier.A2, wmtStaff.workload, 6)
    val bTierTotal = setupWmtTierTotal(Tier.B2, wmtStaff.workload, 6)
    val cTierTotal = setupWmtTierTotal(Tier.C2, wmtStaff.workload, 6)
    val dTierTotal = setupWmtTierTotal(Tier.D2, wmtStaff.workload, 6)
    val untieredTotal = setupWmtTierTotal(null, wmtStaff.workload, 6)

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
      .isEqualTo(50)
      .jsonPath("$.code")
      .isEqualTo("OM1")
      .jsonPath("$.email")
      .isEqualTo("sheila.hancock@test.justice.gov.uk")
      .jsonPath("$.totalCases")
      .isEqualTo(35)
      .jsonPath("$.weeklyHours")
      .isEqualTo(wmtStaff.wmtWorkloadOwnerEntity.contractedHours)
      .jsonPath("$.totalReductionHours")
      .isEqualTo(reduction.hours)
      .jsonPath("$.pointsAvailable")
      .isEqualTo(wmtStaff.workloadPointsCalculation.availablePoints)
      .jsonPath("$.pointsUsed")
      .isEqualTo(wmtStaff.workloadPointsCalculation.totalPoints)
      .jsonPath("$.pointsRemaining")
      .isEqualTo(wmtStaff.workloadPointsCalculation.availablePoints - wmtStaff.workloadPointsCalculation.totalPoints)
      .jsonPath("$.lastUpdatedOn")
      .isEqualTo(
        wmtStaff.workloadPointsCalculation.lastUpdatedOn.format(
          DateTimeFormatter.ISO_LOCAL_DATE_TIME
        )
      )
      .jsonPath("$.nextReductionChange")
      .isEqualTo(
        reduction.effectiveTo.withZoneSameInstant(ZoneOffset.UTC).format(
          DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
      )
      .jsonPath("$.caseTotals.a")
      .isEqualTo(aTierTotal.totalFilteredCases)
      .jsonPath("$.caseTotals.b")
      .isEqualTo(bTierTotal.totalFilteredCases)
      .jsonPath("$.caseTotals.c")
      .isEqualTo(cTierTotal.totalFilteredCases)
      .jsonPath("$.caseTotals.d")
      .isEqualTo(dTierTotal.totalFilteredCases)
      .jsonPath("$.caseTotals.untiered")
      .isEqualTo(untieredTotal.totalFilteredCases)
      .jsonPath("$.paroleReportsDue")
      .isEqualTo(wmtStaff.workload.institutionalReportsDueInNextThirtyDays)
      .jsonPath("$.caseEndDue")
      .isEqualTo(1)
      .jsonPath("$.releasesDue")
      .isEqualTo(1)
      .jsonPath("$.lastAllocatedEvent")
      .doesNotExist()
  }

  @Test
  fun `can get overview for an offender manager without any reductions`() {
    val teamCode = "T1"
    val offenderManagerCode = "OM2"
    setupCurrentWmtStaff(offenderManagerCode, teamCode)
    communityApi.staffCodeResponse(offenderManagerCode, teamCode)
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
    communityApi.staffCodeResponse(offenderManagerCode, teamCode)
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

  @Test
  fun `can get overview for an offender manager without email`() {
    val teamCode = "T1"
    val offenderManagerCode = "NOWORKLOAD1"
    communityApi.staffCodeResponse(offenderManagerCode, teamCode, "EX", null)
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
      .jsonPath("$.email")
      .doesNotExist()
  }

  @Test
  fun `get last allocated event`() {
    val teamCode = "T1"
    val offenderManagerCode = "NOWORKLOAD1"
    communityApi.staffCodeResponse(offenderManagerCode, teamCode)
    val eventManager = eventManagerRepository.save(EventManagerEntity(crn = "CRN12345", eventId = BigInteger.TEN, staffId = BigInteger.ONE, staffCode = offenderManagerCode, teamCode = teamCode, createdBy = "USER1", providerCode = "PV1", isActive = true, eventNumber = null))
    val storedEventManager = eventManagerRepository.findByIdOrNull(eventManager.id!!)!!
    val caseDetails = caseDetailsRepository.save(CaseDetailsEntity(storedEventManager.crn, Tier.C3, CaseType.COMMUNITY, "Jane", "Doe"))
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers/$offenderManagerCode")
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.lastAllocatedEvent.allocatedOn")
      .isEqualTo(
        storedEventManager.createdDate!!.withZoneSameInstant(ZoneOffset.UTC).format(
          DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
      )
      .jsonPath("$.lastAllocatedEvent.tier")
      .isEqualTo(caseDetails.tier.name)
      .jsonPath("$.lastAllocatedEvent.sentenceType")
      .isEqualTo(caseDetails.type.name)
  }
}
