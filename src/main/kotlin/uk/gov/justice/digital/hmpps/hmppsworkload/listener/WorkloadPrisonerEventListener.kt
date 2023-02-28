package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveCaseDetailsService

@Component
class WorkloadPrisonerEventListener(
  private val objectMapper: ObjectMapper,
  private val saveCaseDetailsService: SaveCaseDetailsService
) {

  @JmsListener(destination = "workloadprisonerqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val nomsNumber = getNomsNumber(rawMessage)
    saveCaseDetailsService.saveByNoms(nomsNumber)
  }

  private fun getNomsNumber(rawMessage: String): String {
    val (message) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    val event = objectMapper.readValue(message, WorkloadPrisonerEvent::class.java)
    return event.personReference.identifiers.find { it.type == "NOMS" }!!.value
  }
}

data class WorkloadPrisonerEvent(
  val personReference: PersonReference
)
