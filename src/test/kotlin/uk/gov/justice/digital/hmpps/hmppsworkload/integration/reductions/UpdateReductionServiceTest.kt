package uk.gov.justice.digital.hmpps.hmppsworkload.integration.reductions

import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.WMTStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionReasonEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService
import java.math.BigDecimal
import java.time.ZonedDateTime

class UpdateReductionServiceTest : IntegrationTestBase() {

  @Autowired
  lateinit var getReductionService: GetReductionService

  @Autowired
  lateinit var updateReductionService: UpdateReductionService

  lateinit var wmtStaff: WMTStaff
  lateinit var reductionReason: ReductionReasonEntity

  @BeforeEach
  fun setupReductionTestData() {
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    wmtStaff = setupCurrentWmtStaff("STAFF2", "TEAM2")
  }

  @Test
  fun `can update incorrect Active reduction to Archived`() {
    val activeReductionWhichIsNowArchived = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(2), effectiveTo = ZonedDateTime.now().minusDays(1), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    Assertions.assertEquals(ReductionStatus.ARCHIVED, reductionsRepository.findByIdOrNull(activeReductionWhichIsNowArchived.id!!)?.status)
  }

  @Test
  fun `can update incorrect Scheduled reduction to be Active`() {
    val scheduledReductionWhichIsNowActive = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(1), status = ReductionStatus.SCHEDULED, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    Assertions.assertEquals(ReductionStatus.ACTIVE, reductionsRepository.findByIdOrNull(scheduledReductionWhichIsNowActive.id!!)?.status)
  }

  @Test
  fun `do nothing when reduction status is correct`() {
    val activeReduction = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(1), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    Assertions.assertEquals(ReductionStatus.ACTIVE, reductionsRepository.findByIdOrNull(activeReduction.id!!)?.status)
  }

  @Test
  fun `can publish out of date reductions when updating reduction status`() {
    reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(1), status = ReductionStatus.SCHEDULED, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    await untilCallTo { verifyReductionsCompletedOnQueue() } matches { it == true }
  }

  @Test
  fun `out of date reductions message contain required data`() {
    reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(1), status = ReductionStatus.SCHEDULED, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    await untilCallTo { verifyReductionsCompletedOnQueue() } matches { it == true }

    val reductionCompleteMessage = getReductionsCompletedMessages()

    Assertions.assertEquals("OUT_OF_DATE_REDUCTIONS", reductionCompleteMessage.eventType)
  }
}
