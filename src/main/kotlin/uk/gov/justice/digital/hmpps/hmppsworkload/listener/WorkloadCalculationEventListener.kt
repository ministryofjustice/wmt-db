package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
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

  @SqsListener("workloadcalculationqueue", factory = "hmppsQueueContainerFactoryProxy")
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
    val message = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    log.info("Received message from workloadcalculationqueue with messageId:{}", message?.messageId)
    return objectMapper.readValue(message.message, WorkloadCalculationEvent::class.java)
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

data class WorkloadCalculationEvent(
  val additionalInformation: AdditionalInformation,
  val personReference: PersonReference,
)

data class AdditionalInformation(
  val availableHours: BigDecimal,
)
