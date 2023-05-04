package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import io.awspring.cloud.sqs.annotation.SqsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.UpdateReductionService

@Component
class ExtractPlacedEventListener(
  private val updateReductionService: UpdateReductionService,
) {

  @SqsListener("hmppsextractplacedqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    CoroutineScope(Dispatchers.Default).future {
      updateReductionService.updateOutOfDateReductionStatus()
    }.get()
  }
}
