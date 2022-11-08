package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WorkloadCalculationService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.GetPersonManager

@Component
class WorkloadPrisonerEventListener(
  private val objectMapper: ObjectMapper,
  private val workloadCalculationService: WorkloadCalculationService,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val getPersonManager: GetPersonManager
) {

  @JmsListener(destination = "workloadprisonerqueue", containerFactory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    val nomsNumber = getNomsNumber(rawMessage)
    communityApiClient.getCrn(nomsNumber).block()?.let { crn ->
      getPersonManager.findLatestByCrn(crn)?.let { personManager ->
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
