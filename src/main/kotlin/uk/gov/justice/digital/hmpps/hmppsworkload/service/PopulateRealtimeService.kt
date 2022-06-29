package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sqs.model.MessageAttributeValue
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.controller.CaseCsv
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.HmppsOffenderEvent
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.SQSMessage
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigInteger

@Service
class PopulateRealtimeService(
  private val hmppsQueueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient
) {

  private val hmppsOffenderQueue by lazy { hmppsQueueService.findByQueueId("hmppsoffenderqueue") ?: throw MissingQueueException("HmppsQueue hmppsoffenderqueue not found") }

  private val hmppsOffenderSqsClient by lazy { hmppsOffenderQueue.sqsClient }

  @Async
  fun sendEvents(cases: List<CaseCsv>) {
    cases
      .map { publishToHmppsOffenderQueue(it) }
      .forEach {
        it.onErrorResume { Mono.empty() }
          .block()
      }
  }

  private fun publishToHmppsOffenderQueue(case: CaseCsv): Mono<Any> {
    return communityApiClient.getAllConvictions(case.crn!!)
      .map { convictions ->
        convictions.filter { it.sentence != null }
          .map { it.sentence!! }
          .forEach {
            val sendMessage = SendMessageRequest(
              hmppsOffenderQueue.queueUrl,
              objectMapper.writeValueAsString(
                caseToOffenderSqsMessage(case.crn!!, it.sentenceId)
              )
            ).withMessageAttributes(
              mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
            )
            log.info("publishing event type {} for crn {}", "SENTENCE_CHANGED", case.crn)
            hmppsOffenderSqsClient.sendMessage(sendMessage)
          }
      }
  }

  private fun caseToOffenderSqsMessage(crn: String, sentenceId: BigInteger): SQSMessage = SQSMessage(
    objectMapper.writeValueAsString(
      HmppsOffenderEvent(crn, sentenceId)
    )
  )

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
