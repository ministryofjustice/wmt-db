package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.HmppsOffenderEvent
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.SQSMessage
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException

@Service
class PopulateRealtimeService(
  private val hmppsQueueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
  @Qualifier("hmppsoffenderqueue-sqs-client") private val hmppsOffenderSqsClient: AmazonSQSAsync,
  private val caseDetailsRepository: CaseDetailsRepository
) {

  private val hmppsOffenderQueueUrl by lazy { hmppsQueueService.findByQueueId("hmppsoffenderqueue")?.queueUrl ?: throw MissingQueueException("HmppsQueue hmppsoffenderqueue not found") }

  @Async
  fun populateEventsFromCaseDetails() {
    caseDetailsRepository.findAll().forEach { caseDetailsEntity ->
      publishToHmppsOffenderQueue(caseDetailsEntity.crn)
    }
  }

  private fun publishToHmppsOffenderQueue(crn: String) {
    val sendMessage = SendMessageRequest(
      hmppsOffenderQueueUrl,
      objectMapper.writeValueAsString(
        caseToOffenderSqsMessage(crn)
      )
    ).withMessageAttributes(
      mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("OFFENDER_DETAILS_CHANGED"))
    )
    log.info("publishing event type {} for crn {}", "OFFENDER_DETAILS_CHANGED", crn)
    hmppsOffenderSqsClient.sendMessage(sendMessage)
  }

  private fun caseToOffenderSqsMessage(crn: String): SQSMessage = SQSMessage(
    objectMapper.writeValueAsString(
      HmppsOffenderEvent(crn, null)
    )
  )

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
