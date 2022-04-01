package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsPersonAllocationMessage
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingTopicException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class SuccessUpdater(
  val hmppsQueueService: HmppsQueueService,
  val objectMapper: ObjectMapper
) {

  private val allocationCompleteTopic by lazy {
    hmppsQueueService.findByTopicId("hmppsallocationcompletetopic")
      ?: throw MissingTopicException("hmppsallocationcompletetopic not found")
  }

  fun updatePerson(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime) {
    val hmppsPersonEvent = HmppsMessage(
      "PERSON_MANAGER_ALLOCATED", 1, "Person allocated event", "http://dummy.com",
      timeUpdated.format(
        DateTimeFormatter.ISO_INSTANT
      ),
      HmppsPersonAllocationMessage(allocationId, crn)
    )
    allocationCompleteTopic.snsClient.publish(
      PublishRequest(allocationCompleteTopic.arn, objectMapper.writeValueAsString(hmppsPersonEvent))
        .withMessageAttributes(mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue(hmppsPersonEvent.eventType)))
    )
  }
}
