package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveCaseDetailsService

@Component
class WorkloadPrisonerEventListener(
  private val objectMapper: ObjectMapper,
  private val saveCaseDetailsService: SaveCaseDetailsService,
) {

  @SqsListener("workloadprisonerqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val nomsNumber = getNomsNumber(rawMessage)
    CoroutineScope(Dispatchers.Default).future {
      saveCaseDetailsService.saveByNoms(nomsNumber)
    }.get()
  }

  private fun getNomsNumber(rawMessage: String): String {
    val (message, messageId) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    val queueName = System.getenv("HMPPS_SQS_QUEUES_WORKLOADPRISONERQUEUE_QUEUE_NAME") ?: "Queue name not found"
    val event = objectMapper.readValue(message, WorkloadPrisonerEvent::class.java)
    log.info("Received message from {$queueName} with messageId :{$messageId}")
    return event.personReference.identifiers.find { it.type == "NOMS" }!!.value
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

data class WorkloadPrisonerEvent(
  val personReference: PersonReference,
)
