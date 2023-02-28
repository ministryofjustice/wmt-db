package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SaveCaseDetailsService

@Component
class TierCalculationEventListener(
  private val objectMapper: ObjectMapper,
  private val saveCaseDetailsService: SaveCaseDetailsService
) {

  @JmsListener(destination = "tiercalcqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val calculationEventData = readMessage(rawMessage)
    saveCaseDetailsService.saveByCrn(crnFrom(calculationEventData))
  }

  private fun readMessage(wrapper: String?): CalculationEventData {
    val (message) = objectMapper.readValue(wrapper, Message::class.java)
    return objectMapper.readValue(message, CalculationEventData::class.java)
  }

  private fun crnFrom(calculationEventData: CalculationEventData) =
    calculationEventData.personReference.identifiers.first { it.type == "CRN" }.value

  data class CalculationEventData(val personReference: PersonReference)

  data class Message(@JsonProperty("Message") val message: String)
}
