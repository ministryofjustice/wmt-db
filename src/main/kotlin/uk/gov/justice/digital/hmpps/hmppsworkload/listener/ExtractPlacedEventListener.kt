package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.deliusToStaffGrade
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService

@Component
class ExtractPlacedEventListener(
  private val updateReductionService: UpdateReductionService,
  private val workloadCalculationService: WorkloadCalculationService,
) {

  @JmsListener(destination = "hmppsextractplacedqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val outOfDateReductions = updateReductionService.updateOutOfDateReductionStatus()

    val staffChanged = getDistinctStaffChanged(outOfDateReductions)

    staffChanged.forEach { workloadOwner ->
      workloadCalculationService.calculate(workloadOwner.offenderManager.code, workloadOwner.team.code, deliusToStaffGrade(workloadOwner.offenderManager.offenderManagerType.gradeCode))
    }
  }

  private fun getDistinctStaffChanged(outOfDateReductions: OutOfDateReductions): Set<WMTWorkloadOwnerEntity> = (outOfDateReductions.activeNowArchived + outOfDateReductions.scheduledNowActive)
    .map { it.workloadOwner }
    .toSet()
}
