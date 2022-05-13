package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveSentenceService
import java.math.BigInteger

@Component
class OffenderEventListener(
  private val objectMapper: ObjectMapper,
  private val saveSentenceService: SaveSentenceService
) {

  @JmsListener(destination = "hmppsoffenderqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val case = getCase(rawMessage)
    log.info("received offender event for crn: {}", case.crn)
    saveSentenceService.saveSentence(case.crn, case.sourceId)
  }

  @MessageExceptionHandler()
  fun errorHandler(e: Exception, msg: String) {
    log.warn("Failed to process sentence change with CRN ${getCase(msg).crn} with error: ${e.message}")
    throw e
  }

  private fun getCase(rawMessage: String): HmppsOffenderEvent {
    val (message) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    return objectMapper.readValue(message, HmppsOffenderEvent::class.java)
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

data class HmppsOffenderEvent(
  val crn: String,
  val sourceId: BigInteger
)

data class SQSMessage(
  @JsonProperty("Message") val message: String,

)
