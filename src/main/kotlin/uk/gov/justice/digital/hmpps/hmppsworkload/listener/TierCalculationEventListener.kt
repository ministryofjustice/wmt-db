package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.annotation.JsonProperty
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
class TierCalculationEventListener(
  private val objectMapper: ObjectMapper,
  private val saveCaseDetailsService: SaveCaseDetailsService,
) {

  @SqsListener("tiercalcqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val calculationEventData = readMessage(rawMessage)
    CoroutineScope(Dispatchers.Default).future {
      saveCaseDetailsService.saveByCrn(crnFrom(calculationEventData))
    }.get()
  }

  private fun readMessage(wrapper: String?): CalculationEventData {
    val message = objectMapper.readValue(wrapper, SQSMessage::class.java)
    log.info("Received message from tiercalcqueue with messageId:{}", message?.messageId)
    return objectMapper.readValue(message.message, CalculationEventData::class.java)
  }

  private fun crnFrom(calculationEventData: CalculationEventData) =
    calculationEventData.personReference.identifiers.first { it.type == "CRN" }.value

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }

  data class CalculationEventData(val personReference: PersonReference)

  data class Message(@JsonProperty("Message") val message: String)
}
