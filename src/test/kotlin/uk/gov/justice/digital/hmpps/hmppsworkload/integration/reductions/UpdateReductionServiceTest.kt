package uk.gov.justice.digital.hmpps.hmppsworkload.integration.reductions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
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
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService
import java.math.BigDecimal
import java.time.ZonedDateTime

class UpdateReductionServiceTest : IntegrationTestBase() {

  @Autowired
  lateinit var getReductionService: GetReductionService

  @Autowired
  lateinit var updateReductionService: UpdateReductionService

  lateinit var workloadOwner: WMTWorkloadOwnerEntity
  lateinit var reductionReason: ReductionReasonEntity

  @BeforeEach
  fun setupReductionTestData() {
    val region = regionRepository.save(RegionEntity(code = "REGION2", description = "Region 2"))
    val pdu = pduRepository.save(PduEntity(code = "LDU2", description = "Local Delivery Unit 2(Actually a Probation Delivery Unit)", region = region))
    val team = teamRepository.save(TeamEntity(code = "TEAM2", description = "Team 2", ldu = pdu))
    val offenderManager = offenderManagerRepository.save(OffenderManagerEntity(code = "STAFFCODE2", forename = "Jane", surname = "Doe", typeId = 1))
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    workloadOwner = wmtWorkloadOwnerRepository.save(WMTWorkloadOwnerEntity(offenderManager = offenderManager, team = team, contractedHours = BigDecimal.valueOf(37.5)))
  }

  @Test
  fun `can update incorrect Active reduction to Archived`() {
    val activeReductionWhichIsNowArchived = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(2), effectiveTo = ZonedDateTime.now().minusDays(1), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    Assertions.assertEquals(ReductionStatus.ARCHIVED, reductionsRepository.findByIdOrNull(activeReductionWhichIsNowArchived.id!!)?.status)
  }

  @Test
  fun `can update incorrect Scheduled reduction to be Active`() {
    val scheduledReductionWhichIsNowActive = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(1), status = ReductionStatus.SCHEDULED, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    Assertions.assertEquals(ReductionStatus.ACTIVE, reductionsRepository.findByIdOrNull(scheduledReductionWhichIsNowActive.id!!)?.status)
  }

  @Test
  fun `do nothing when reduction status is correct`() {
    val activeReduction = reductionsRepository.save(ReductionEntity(workloadOwner = workloadOwner, hours = BigDecimal.valueOf(3.2), effectiveFrom = ZonedDateTime.now().minusDays(1), effectiveTo = ZonedDateTime.now().plusDays(1), status = ReductionStatus.ACTIVE, reductionReasonId = reductionReason.id!!))
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())

    Assertions.assertEquals(ReductionStatus.ACTIVE, reductionsRepository.findByIdOrNull(activeReduction.id!!)?.status)
  }
}
