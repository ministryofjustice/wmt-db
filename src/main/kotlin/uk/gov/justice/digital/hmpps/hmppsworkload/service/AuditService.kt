package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.SQSMessage
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigInteger
import java.time.LocalDateTime

@Service
class AuditService(
  private val hmppsQueueService: HmppsQueueService,
  private val telemetryClient: TelemetryClient,
  private val objectMapper: ObjectMapper,
  @Qualifier("hmppsauditqueue-sqs-client") private val hmppsAuditSqsClient: AmazonSQSAsync
) {

  private val hmppsAuditQueueUrl by lazy { hmppsQueueService.findByQueueId("hmppsauditqueue")?.queueUrl ?: throw MissingQueueException("HmppsQueue hmppsauditqueue not found") }

  @Async
  fun publishToHmppsAuditQueue(crn: String, eventId: BigInteger, loggedInUser: String, requirementIds: List<BigInteger>) {
    val auditData = AuditData(
      crn,
      eventId,
      requirementIds
    )

    val sendMessage = SendMessageRequest(
      hmppsAuditQueueUrl,
      objectMapper.writeValueAsString(
        sqsMessage(
          HmppsAuditMessage(operationId = telemetryClient.context.operation.id, who = loggedInUser, details = auditData)
        )
      )
    )
    hmppsAuditSqsClient.sendMessage(sendMessage)
  }

  private fun sqsMessage(hmppsAuditMessage: HmppsAuditMessage<AuditData>) =
    SQSMessage(objectMapper.writeValueAsString(hmppsAuditMessage))
}

data class HmppsAuditMessage<AuditData>(val operationId: String, val what: String = "CASE_ALLOCATED", val `when`: LocalDateTime = LocalDateTime.now(), val who: String, val service: String = "hmpps-workload", val details: AuditData)

data class AuditData(
  val crn: String,
  val eventId: BigInteger,
  val requirementIds: List<BigInteger>
)
