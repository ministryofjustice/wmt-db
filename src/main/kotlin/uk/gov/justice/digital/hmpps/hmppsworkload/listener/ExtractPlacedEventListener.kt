package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffTeam
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService

@Component
class ExtractPlacedEventListener(
  private val updateReductionService: UpdateReductionService,
) {

  @JmsListener(destination = "hmppsextractplacedqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val updatedReductions = updateReductionService.updateOutOfDateReductionStatus()
  }

  private fun getAllStaff(outOfDateReductions: OutOfDateReductions): Set<StaffTeam> = (outOfDateReductions.activeNowArchived + outOfDateReductions.scheduledNowActive)
    .map { reductionEntity -> StaffTeam(reductionEntity.workloadOwner.offenderManager.code, reductionEntity.workloadOwner.team.code) }
    .toSet()
}
