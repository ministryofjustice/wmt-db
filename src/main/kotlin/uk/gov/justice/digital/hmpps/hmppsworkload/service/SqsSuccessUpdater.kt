package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsAllocationMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReferenceType
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingTopicException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
@ConditionalOnProperty("hmpps.sqs.topics.hmmppsdomaintopic.arn")
class SqsSuccessUpdater(
  val hmppsQueueService: HmppsQueueService,
  val objectMapper: ObjectMapper,
  @Value("\${ingress.url}") private val ingressUrl: String,
  @Value("\${person.manager.getByIdPath}") private val personManagerLookupPath: String,
  @Value("\${event.manager.getByIdPath}") private val eventManagerLookupPath: String,
  @Value("\${requirement.manager.getByIdPath}") private val requirementManagerLookupPath: String
) : SuccessUpdater {

  private val allocationCompleteTopic by lazy {
    hmppsQueueService.findByTopicId("hmmppsdomaintopic")
      ?: throw MissingTopicException("hmmppsdomaintopic not found")
  }

  override fun updatePerson(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime) {
    val hmppsPersonEvent = HmppsMessage(
      "person.community.manager.allocated", 1, "Person allocated event", generateDetailsUri(personManagerLookupPath, allocationId),
      timeUpdated.format(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
      ),
      HmppsAllocationMessage(allocationId),
      PersonReference(listOf(PersonReferenceType("CRN", crn)))
    )
    allocationCompleteTopic.snsClient.publish(
      PublishRequest(allocationCompleteTopic.arn, objectMapper.writeValueAsString(hmppsPersonEvent))
        .withMessageAttributes(mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue(hmppsPersonEvent.eventType)))
    ).also {
      log.info("Published event {} to topic for CRN {} and id {}", hmppsPersonEvent.eventType, crn, allocationId)
    }
  }

  private fun generateDetailsUri(path: String, allocationId: UUID): String = UriComponentsBuilder.newInstance().scheme("https").host(ingressUrl).path(path).buildAndExpand(allocationId).toUriString()

  override fun updateEvent(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime) {
    val hmppsEventAllocatedEvent = HmppsMessage(
      "event.manager.allocated", 1, "Event allocated event", generateDetailsUri(eventManagerLookupPath, allocationId),
      timeUpdated.format(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
      ),
      HmppsAllocationMessage(allocationId),
      PersonReference(listOf(PersonReferenceType("CRN", crn)))
    )
    allocationCompleteTopic.snsClient.publish(
      PublishRequest(allocationCompleteTopic.arn, objectMapper.writeValueAsString(hmppsEventAllocatedEvent))
        .withMessageAttributes(mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue(hmppsEventAllocatedEvent.eventType)))
    ).also {
      log.info("Published event {} to topic for CRN {} and id {}", hmppsEventAllocatedEvent.eventType, crn, allocationId)
    }
  }

  override fun updateRequirement(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime) {
    val hmppsRequirementAllocatedEvent = HmppsMessage(
      "requirement.manager.allocated", 1, "Requirement allocated event", generateDetailsUri(requirementManagerLookupPath, allocationId),
      timeUpdated.format(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
      ),
      HmppsAllocationMessage(allocationId),
      PersonReference(listOf(PersonReferenceType("CRN", crn)))
    )
    allocationCompleteTopic.snsClient.publish(
      PublishRequest(allocationCompleteTopic.arn, objectMapper.writeValueAsString(hmppsRequirementAllocatedEvent))
        .withMessageAttributes(mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue(hmppsRequirementAllocatedEvent.eventType)))
    ).also {
      log.info("Published event {} to topic for CRN {} and id {}", hmppsRequirementAllocatedEvent.eventType, crn, allocationId)
    }
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
