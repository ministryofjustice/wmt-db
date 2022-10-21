package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService

@Component
class ExtractPlacedEventListener(
  private val updateReductionService: UpdateReductionService,
  private val getReductionService: GetReductionService
) {

  @JmsListener(destination = "hmppsextractplacedqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    updateReductionService.updateReductionStatus(getReductionService.findOutOfDateReductions())
  }
}
