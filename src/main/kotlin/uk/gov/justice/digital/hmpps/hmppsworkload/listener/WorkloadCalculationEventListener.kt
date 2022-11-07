package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import java.math.BigDecimal

@Component
class WorkloadCalculationEventListener(
  private val objectMapper: ObjectMapper,
  private val workloadCalculationService: WorkloadCalculationService,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient
) {

  @JmsListener(destination = "workloadcalculationqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val workloadCalculationEvent = getWorkloadCalculationEvent(rawMessage)
    val availableHours = workloadCalculationEvent.additionalInformation.availableHours
    val staffCode = workloadCalculationEvent.personReference.identifiers.find { it.type == "staffCode" }!!.value
    val teamCode = workloadCalculationEvent.personReference.identifiers.find { it.type == "teamCode" }!!.value
    val staffGrade = communityApiClient.getStaffByCode(staffCode).map { it.grade }.block()!!
    workloadCalculationService.calculate(staffCode, teamCode, staffGrade, availableHours)
  }

  private fun getWorkloadCalculationEvent(rawMessage: String): WorkloadCalculationEvent {
    val (message) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    return objectMapper.readValue(message, WorkloadCalculationEvent::class.java)
  }
}

data class WorkloadCalculationEvent(
  val additionalInformation: AdditionalInformation,
  val personReference: PersonReference
)

data class AdditionalInformation(
  val availableHours: BigDecimal
)
