package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveCaseDetailsService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveSentenceService
import java.math.BigInteger

@Component
class OffenderEventListener(
  private val objectMapper: ObjectMapper,
  private val saveSentenceService: SaveSentenceService,
  private val saveCaseDetailsService: SaveCaseDetailsService
) {

  @JmsListener(destination = "hmppsoffenderqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val (crn, sentenceId) = getCase(rawMessage)
    sentenceId?.run {
      saveSentenceService.saveSentence(crn, sentenceId)
    }
    saveCaseDetailsService.save(crn)
  }

  private fun getCase(rawMessage: String): HmppsOffenderEvent {
    val (message) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    return objectMapper.readValue(message, HmppsOffenderEvent::class.java)
  }
}

data class HmppsOffenderEvent(
  val crn: String,
  @JsonProperty("sourceId") val sentenceId: BigInteger?
)

data class SQSMessage(
  @JsonProperty("Message") val message: String,
)
