package uk.gov.justice.digital.hmpps.hmppsworkload.integration.reductions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionReasonEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.OffenderManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PduEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RegionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.TeamEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
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
    val region = regionRepository.save(RegionEntity(code = "REGION1", description = "Region 1"))
    val pdu = pduRepository.save(PduEntity(code = "LDU1", description = "Local Delivery Unit (Actually a Probation Delivery Unit)", region = region))
    val team = teamRepository.save(TeamEntity(code = "TEAM1", description = "Team 1", ldu = pdu))
    val offenderManager = offenderManagerRepository.save(OffenderManagerEntity(code = "STAFFCODE1", forename = "Jane", surname = "Doe", typeId = 1))
    val workloadOwner = wmtWorkloadOwnerRepository.save(WMTWorkloadOwnerEntity(offenderManager = offenderManager, team = team, contractedHours = BigDecimal.valueOf(37.5)))
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    val reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    val activeReductionInPast = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(7), effectiveTo = ZonedDateTime.now().minusDays(1), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))

    val activeReduction = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(5), effectiveFrom = ZonedDateTime.now().minusDays(7), effectiveTo = ZonedDateTime.now().plusDays(7), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))

    val deletedReductionInPast = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(7), effectiveTo = ZonedDateTime.now().minusDays(1), status = ReductionStatus.DELETED, reductionReasonId = reductionReason.id!!))

    val results = getReductionService.findOutOfDateReductions()

    Assertions.assertThat(results.activeNowArchived).extracting("id")
      .contains(activeReductionInPast.id!!)

    Assertions.assertThat(results.activeNowArchived).extracting("id")
      .doesNotContain(activeReduction.id!!, deletedReductionInPast.id!!)
  }

  @Test
  fun `must return all scheduled reductions which have an effective from in the past and effective to in the future`() {
    val region = regionRepository.save(RegionEntity(code = "REGION2", description = "Region 2"))
    val pdu = pduRepository.save(PduEntity(code = "LDU2", description = "Local Delivery Unit 2(Actually a Probation Delivery Unit)", region = region))
    val team = teamRepository.save(TeamEntity(code = "TEAM2", description = "Team 2", ldu = pdu))
    val offenderManager = offenderManagerRepository.save(OffenderManagerEntity(code = "STAFFCODE2", forename = "Jane", surname = "Doe", typeId = 1))
    val workloadOwner = wmtWorkloadOwnerRepository.save(WMTWorkloadOwnerEntity(offenderManager = offenderManager, team = team, contractedHours = BigDecimal.valueOf(37.5)))
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    val reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    val scheduledReductionWhichIsNowActive = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(3), status = ReductionStatus.SCHEDULED, reductionReasonId = reductionReason.id!!))

    val results = getReductionService.findOutOfDateReductions()

    Assertions.assertThat(results.scheduledNowActive).extracting("id")
      .contains(scheduledReductionWhichIsNowActive.id!!)
  }
}
