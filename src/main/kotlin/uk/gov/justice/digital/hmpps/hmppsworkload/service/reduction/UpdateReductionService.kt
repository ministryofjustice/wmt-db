package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.deliusToStaffGrade
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SuccessUpdater
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class UpdateReductionService(
  private val reductionsRepository: ReductionsRepository,
  private val successUpdater: SuccessUpdater,
  private val workloadCalculationService: WorkloadCalculationService,

) {

  @Transactional
  fun updateOutOfDateReductionStatus() {
    val outOfDateReductions = findOutOfDateReductions()

    outOfDateReductions.activeNowArchived
      .onEach { it.status = ReductionStatus.ARCHIVED }
      .let { reductionsRepository.saveAll(it) }
    outOfDateReductions.scheduledNowActive
      .onEach { it.status = ReductionStatus.ACTIVE }
      .let { reductionsRepository.saveAll(it) }

    successUpdater.outOfDateReductionsProcessed()
    val staffWeeklyHoursChanged = getDistinctStaffChanged(outOfDateReductions)

    staffWeeklyHoursChanged.forEach { workloadOwner ->
      workloadCalculationService.calculate(workloadOwner.offenderManager.code, workloadOwner.team.code, deliusToStaffGrade(workloadOwner.offenderManager.offenderManagerType.gradeCode))
    }
  }

  private fun getDistinctStaffChanged(outOfDateReductions: OutOfDateReductions): Set<WMTWorkloadOwnerEntity> = (outOfDateReductions.activeNowArchived + outOfDateReductions.scheduledNowActive)
    .map { it.workloadOwner }
    .toSet()

  private fun findOutOfDateReductions(): OutOfDateReductions = OutOfDateReductions(
    reductionsRepository.findByEffectiveFromBeforeAndEffectiveToAfterAndStatus(ZonedDateTime.now(), ZonedDateTime.now(), ReductionStatus.SCHEDULED),
    reductionsRepository.findByEffectiveToBeforeAndStatus(ZonedDateTime.now(), ReductionStatus.ACTIVE)
  )
}
