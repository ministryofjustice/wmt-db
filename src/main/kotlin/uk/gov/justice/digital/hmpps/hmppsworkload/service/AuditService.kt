package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigInteger
import java.time.Instant
import java.util.UUID

@Service
class AuditService(
  private val queueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
  @Qualifier("hmppsauditqueue-sqs-client") private val sqsClient: AmazonSQSAsync
) {

  private val hmppsAuditQueueUrl by lazy { queueService.findByQueueId("hmppsauditqueue")?.queueUrl ?: throw MissingQueueException("HmppsQueue hmppsauditqueue not found") }

  @Async
  fun publishToAuditQueue(crn: String, eventId: BigInteger, loggedInUser: String, requirementIds: List<BigInteger>) {
    val auditData = AuditData(
      crn,
      eventId,
      requirementIds
    )

    val sendMessage = SendMessageRequest(
      hmppsAuditQueueUrl,
      objectMapper.writeValueAsString(
        AuditMessage(operationId = UUID.randomUUID().toString(), who = loggedInUser, details = auditData)
      )
    )
    sqsClient.sendMessage(sendMessage)
  }
}

data class AuditMessage<AuditData>(val operationId: String, val what: String = "CASE_ALLOCATED", val `when`: Instant = Instant.now(), val who: String, val service: String = "hmpps-workload", val details: AuditData)

data class AuditData(
  val crn: String,
  val eventId: BigInteger,
  val requirementIds: List<BigInteger>
)
