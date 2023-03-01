package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService

@Component
class ExtractPlacedEventListener(
  private val updateReductionService: UpdateReductionService
) {

  @JmsListener(destination = "hmppsextractplacedqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    CoroutineScope(Dispatchers.Default).future {
      updateReductionService.updateOutOfDateReductionStatus()
    }.get()
  }
}
