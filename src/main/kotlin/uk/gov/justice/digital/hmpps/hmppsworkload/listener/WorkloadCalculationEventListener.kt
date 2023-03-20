package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import java.math.BigDecimal

@Component
class WorkloadCalculationEventListener(
  private val objectMapper: ObjectMapper,
  private val workloadCalculationService: WorkloadCalculationService,
  @Qualifier("workforceAllocationsToDeliusApiClient") private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient,
) {

  @JmsListener(destination = "workloadcalculationqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val workloadCalculationEvent = getWorkloadCalculationEvent(rawMessage)
    val availableHours = workloadCalculationEvent.additionalInformation.availableHours
    val staffIdentifier = StaffIdentifier(
      workloadCalculationEvent.personReference.identifiers.find { it.type == "staffCode" }!!.value,
      workloadCalculationEvent.personReference.identifiers.find { it.type == "teamCode" }!!.value,
    )
    CoroutineScope(Dispatchers.Default).future {
      val staffGrade = workforceAllocationsToDeliusApiClient.getOfficerView(staffIdentifier.staffCode).getGrade()
      workloadCalculationService.saveWorkloadCalculation(staffIdentifier, staffGrade, availableHours)
    }.get()
  }

  private fun getWorkloadCalculationEvent(rawMessage: String): WorkloadCalculationEvent {
    val (message) = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    return objectMapper.readValue(message, WorkloadCalculationEvent::class.java)
  }
}

data class WorkloadCalculationEvent(
  val additionalInformation: AdditionalInformation,
  val personReference: PersonReference,
)

data class AdditionalInformation(
  val availableHours: BigDecimal,
)
