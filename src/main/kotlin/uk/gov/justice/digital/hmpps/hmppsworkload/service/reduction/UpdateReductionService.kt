package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.SQSMessage
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import javax.transaction.Transactional

@Service
class UpdateReductionService(
  private val reductionsRepository: ReductionsRepository,
  private val getReductionService: GetReductionService,
  private val hmppsQueueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
  @Qualifier("hmppsreductionscompletedqueue-sqs-client") private val hmppsReductionsCompletedSqsClient: AmazonSQSAsync
) {

  private val hmppsReductionsCompletedQueueUrl by lazy { hmppsQueueService.findByQueueId("hmppsreductionscompletedqueue")?.queueUrl ?: throw MissingQueueException("HmppsQueue hmppsreductionscompletedqueue not found") }

  @Transactional
  fun updateReductionStatus(outOfDateReductions: OutOfDateReductions) {

    outOfDateReductions.activeNowArchived
      .onEach { it.status = ReductionStatus.ARCHIVED }
      .let { reductionsRepository.saveAll(it) }
    outOfDateReductions.scheduledNowActive
      .onEach { it.status = ReductionStatus.ACTIVE }
      .let { reductionsRepository.saveAll(it) }

    publishOutOfDateReductionsToHmppsReductionsCompletedQueue()
  }

  private fun publishOutOfDateReductionsToHmppsReductionsCompletedQueue() {
    val sendMessage = SendMessageRequest(
      hmppsReductionsCompletedQueueUrl,
      objectMapper.writeValueAsString(
        outOfDateReductionsToReductionsCompletedSqsMessage()
      )
    ).withMessageAttributes(
      mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("OUT_OF_DATE_REDUCTIONS"))
    )
    log.info("publishing event type {}", "OUT_OF_DATE_REDUCTIONS_COMPLETED")
    hmppsReductionsCompletedSqsClient.sendMessage(sendMessage)
  }

  private fun outOfDateReductionsToReductionsCompletedSqsMessage(): SQSMessage = SQSMessage(
    objectMapper.writeValueAsString(
      getReductionService.findOutOfDateReductions()
    )
  )

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
