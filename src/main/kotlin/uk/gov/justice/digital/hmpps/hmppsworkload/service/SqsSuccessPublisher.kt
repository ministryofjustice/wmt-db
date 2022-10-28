package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import com.amazonaws.services.sqs.model.SendMessageRequest
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
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.StaffAvailableHours
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import uk.gov.justice.hmpps.sqs.MissingTopicException
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

private const val EVENT_TYPE = "eventType"

private const val STRING = "String"

private const val LOG_TEMPLATE = "Published event {} to topic for CRN {} and id {}"

@Service
@ConditionalOnProperty("hmpps.sqs.topics.hmppsdomaintopic.arn")
class SqsSuccessPublisher(
  val hmppsQueueService: HmppsQueueService,
  val objectMapper: ObjectMapper,
  @Value("\${ingress.url}") private val ingressUrl: String,
  @Value("\${person.manager.getByIdPath}") private val personManagerLookupPath: String,
  @Value("\${event.manager.getByIdPath}") private val eventManagerLookupPath: String,
  @Value("\${requirement.manager.getByIdPath}") private val requirementManagerLookupPath: String
) : SuccessUpdater {

  private val domainTopic by lazy {
    hmppsQueueService.findByTopicId("hmppsdomaintopic")
      ?: throw MissingTopicException("hmppsdomaintopic not found")
  }

  private val hmppsReductionsCompletedQueue by lazy {
    hmppsQueueService.findByQueueId("hmppsreductionscompletedqueue")
      ?: throw MissingQueueException("HmppsQueue hmppsreductionsCompletedqueue not found")
  }

  private val hmppsAuditQueue by lazy { hmppsQueueService.findByQueueId("hmppsauditqueue") ?: throw MissingQueueException("HmppsQueue hmppsauditqueue not found") }

  override fun updatePerson(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime) {
    val hmppsPersonEvent = HmppsMessage(
      "person.community.manager.allocated",
      1,
      "Person allocated event",
      generateDetailsUri(personManagerLookupPath, allocationId),
      timeUpdated.format(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
      ),
      HmppsAllocationMessage(allocationId),
      PersonReference(listOf(PersonReferenceType("CRN", crn)))
    )
    domainTopic.snsClient.publish(
      PublishRequest(domainTopic.arn, objectMapper.writeValueAsString(hmppsPersonEvent))
        .withMessageAttributes(mapOf(EVENT_TYPE to MessageAttributeValue().withDataType(STRING).withStringValue(hmppsPersonEvent.eventType)))
    ).also {
      log.info(LOG_TEMPLATE, hmppsPersonEvent.eventType, crn, allocationId)
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
    domainTopic.snsClient.publish(
      PublishRequest(domainTopic.arn, objectMapper.writeValueAsString(hmppsEventAllocatedEvent))
        .withMessageAttributes(mapOf(EVENT_TYPE to MessageAttributeValue().withDataType(STRING).withStringValue(hmppsEventAllocatedEvent.eventType)))
    ).also {
      log.info(LOG_TEMPLATE, hmppsEventAllocatedEvent.eventType, crn, allocationId)
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
    domainTopic.snsClient.publish(
      PublishRequest(domainTopic.arn, objectMapper.writeValueAsString(hmppsRequirementAllocatedEvent))
        .withMessageAttributes(mapOf(EVENT_TYPE to MessageAttributeValue().withDataType(STRING).withStringValue(hmppsRequirementAllocatedEvent.eventType)))
    ).also {
      log.info(LOG_TEMPLATE, hmppsRequirementAllocatedEvent.eventType, crn, allocationId)
    }
  }

  override fun outOfDateReductionsProcessed() {
    val sendMessage = SendMessageRequest(
      hmppsReductionsCompletedQueue.queueUrl,
      getReductionChangeMessage()
    ).withMessageAttributes(
      mapOf("eventType" to com.amazonaws.services.sqs.model.MessageAttributeValue().withDataType("String").withStringValue("OUT_OF_DATE_REDUCTIONS"))
    )
    log.info("publishing event type OUT_OF_DATE_REDUCTIONS")
    hmppsReductionsCompletedQueue.sqsClient.sendMessage(sendMessage)
  }

  override fun auditAllocation(
    crn: String,
    eventId: BigInteger,
    loggedInUser: String,
    requirementIds: List<BigInteger>
  ) {
    val auditData = AuditData(
      crn,
      eventId,
      requirementIds
    )

    val sendMessage = SendMessageRequest(
      hmppsAuditQueue.queueUrl,
      objectMapper.writeValueAsString(
        AuditMessage(operationId = UUID.randomUUID().toString(), who = loggedInUser, details = objectMapper.writeValueAsString(auditData))
      )
    )
    hmppsAuditQueue.sqsClient.sendMessage(sendMessage)
  }
  override fun auditReduction(reductionEntity: ReductionEntity, reductionStatus: String) {
    val reductionsAuditData = ReductionsAuditData(
      reductionEntity.workloadOwner.offenderManager.code,
      reductionEntity.id!!
    )

    val sendMessage = SendMessageRequest(
      hmppsAuditQueue.queueUrl,
      objectMapper.writeValueAsString(
        AuditMessage(operationId = UUID.randomUUID().toString(), who = "system_user", details = objectMapper.writeValueAsString(reductionsAuditData), what = reductionStatus)
      )
    )
    hmppsAuditQueue.sqsClient.sendMessage(sendMessage)
  }
  override fun staffAvailableHoursChange(staffCode: String, teamCode: String, availableHours: BigDecimal) {
    val staffAvailableHoursChangeMessage = HmppsMessage(
      "staff.available.hours.changed", 1, "Staff Available hours changed", "",
      ZonedDateTime.now().format(
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
      ),
      StaffAvailableHours(availableHours),
      PersonReference(listOf(PersonReferenceType("staffCode", staffCode), PersonReferenceType("teamCode", teamCode)))
    )
    domainTopic.snsClient.publish(
      PublishRequest(domainTopic.arn, objectMapper.writeValueAsString(staffAvailableHoursChangeMessage))
        .withMessageAttributes(mapOf(EVENT_TYPE to MessageAttributeValue().withDataType(STRING).withStringValue(staffAvailableHoursChangeMessage.eventType)))
    ).also {
      log.info("staff available hours changed message for {} in team {}", staffCode, teamCode)
    }
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

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}

data class AuditMessage(val operationId: String, val what: String = "CASE_ALLOCATED", val `when`: Instant = Instant.now(), val who: String, val service: String = "hmpps-workload", val details: String)

data class AuditData(
  val crn: String,
  val eventId: BigInteger,
  val requirementIds: List<BigInteger>
)

data class ReductionsAuditData(
  val staffCode: String,
  val reductionId: Long
)
