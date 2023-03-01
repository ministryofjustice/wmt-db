package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveCaseDetailsService

@Component
class OffenderEventListener(
  private val objectMapper: ObjectMapper,
  private val saveCaseDetailsService: SaveCaseDetailsService
) {

  @JmsListener(destination = "hmppsoffenderqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val (crn) = getCase(rawMessage)
    CoroutineScope(Dispatchers.Default).future {
      saveCaseDetailsService.saveByCrn(crn)
    }.get()
  }

  private fun getCase(rawMessage: String): HmppsOffenderEvent {
    val (message) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    return objectMapper.readValue(message, HmppsOffenderEvent::class.java)
  }
}

data class HmppsOffenderEvent(
  val crn: String
)

data class SQSMessage(
  @JsonProperty("Message") val message: String,
)
