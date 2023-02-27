package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.GetPersonManager

@Component
class WorkloadPrisonerEventListener(
  private val objectMapper: ObjectMapper,
  private val workloadCalculationService: WorkloadCalculationService,
  private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient,
  private val getPersonManager: GetPersonManager
) {

  @JmsListener(destination = "workloadprisonerqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val nomsNumber = getNomsNumber(rawMessage)
    workforceAllocationsToDeliusApiClient.getSummaryByCrn(nomsNumber).block()?.let {
      getPersonManager.findLatestByCrn(it.crn)?.let { personManager ->
        workloadCalculationService.saveWorkloadCalculation(StaffIdentifier(personManager.staffCode, personManager.teamCode), personManager.staffGrade)
      }
    }
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
