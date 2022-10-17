package uk.gov.justice.digital.hmpps.hmppsworkload.integration.reductions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionReasonEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService
import java.math.BigDecimal
import java.time.ZonedDateTime

/*
   temporary test until we have an orchestrator. For orchestrator, I thought:
   1. get all out of date reductions
   2. Update all out of date reductions
   3. Map them to a distinct list of staff code, team code, loop through, run calculation
   4. emit event for reduction status finished to kick off wmt-worker ETL process
 */
class GetReductionServiceTests : IntegrationTestBase() {

  @Autowired
  protected lateinit var getReductionService: GetReductionService

  @Test
  fun `must return all active reductions which have an effective to in the past`() {
    val wmtStaff = setupCurrentWmtStaff("STAFF1", "TEAM1")
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    val reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    val activeReductionInPast = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(7), effectiveTo = ZonedDateTime.now().minusDays(1), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))

    val activeReduction = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(5), effectiveFrom = ZonedDateTime.now().minusDays(7), effectiveTo = ZonedDateTime.now().plusDays(7), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))

    val deletedReductionInPast = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(7), effectiveTo = ZonedDateTime.now().minusDays(1), status = ReductionStatus.DELETED, reductionReasonId = reductionReason.id!!))

    val results = getReductionService.findOutOfDateReductions()

    Assertions.assertThat(results.activeNowArchived).extracting("id")
      .contains(activeReductionInPast.id!!)

    Assertions.assertThat(results.activeNowArchived).extracting("id")
      .doesNotContain(activeReduction.id!!, deletedReductionInPast.id!!)
  }

  @Test
  fun `must return all scheduled reductions which have an effective from in the past and effective to in the future`() {
    val wmtStaff = setupCurrentWmtStaff("STAFF2", "TEAM2")
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    val reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    val scheduledReductionWhichIsNowActive = reductionsRepository.save(ReductionEntity(workloadOwner = wmtStaff.wmtWorkloadOwnerEntity, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(3), status = ReductionStatus.SCHEDULED, reductionReasonId = reductionReason.id!!))

    val results = getReductionService.findOutOfDateReductions()

    Assertions.assertThat(results.scheduledNowActive).extracting("id")
      .contains(scheduledReductionWhichIsNowActive.id!!)
  }
}
