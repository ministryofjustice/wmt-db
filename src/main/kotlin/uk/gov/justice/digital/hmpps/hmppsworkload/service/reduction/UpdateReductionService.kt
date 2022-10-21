package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

@Service
class UpdateReductionService(
  private val reductionsRepository: ReductionsRepository,
  private val hmppsQueueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
  @Qualifier("hmppsreductionscompletedqueue-sqs-client") private val hmppsReductionsCompletedSqsClient: AmazonSQS
) {

  private val hmppsReductionsCompletedQueueUrl by lazy { hmppsQueueService.findByQueueId("hmppsreductionscompletedqueue")?.queueUrl ?: throw MissingQueueException("HmppsQueue hmppsreductionscompletedqueue not found") }

  @Transactional
  fun updateOutOfDateReductionStatus() {
    val outOfDateReductions = findOutOfDateReductions()

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
      getReductionChangeMessage()
    ).withMessageAttributes(
      mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("OUT_OF_DATE_REDUCTIONS"))
    )
    log.info("publishing event type OUT_OF_DATE_REDUCTIONS")
    hmppsReductionsCompletedSqsClient.sendMessage(sendMessage)
  }

  private fun getReductionChangeMessage(): String = objectMapper.writeValueAsString(
    HmppsMessage(
      "OUT_OF_DATE_REDUCTIONS",
      1,
      "Out of date reduction status are now correct",
      "",
      ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
      objectMapper.createObjectNode(),
      PersonReference(emptyList())
    )
  )
  private fun findOutOfDateReductions(): OutOfDateReductions = OutOfDateReductions(
    reductionsRepository.findByEffectiveFromBeforeAndEffectiveToAfterAndStatus(ZonedDateTime.now(), ZonedDateTime.now(), ReductionStatus.SCHEDULED),
    reductionsRepository.findByEffectiveToBeforeAndStatus(ZonedDateTime.now(), ReductionStatus.ACTIVE)
  )
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
