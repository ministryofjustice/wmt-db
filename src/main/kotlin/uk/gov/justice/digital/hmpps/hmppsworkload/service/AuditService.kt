package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PduRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionReasonRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.ZonedDateTime
import java.util.UUID

@Service
class AuditService(
  private val queueService: HmppsQueueService,
  private val objectMapper: ObjectMapper,
  private val reductionsRepository: ReductionsRepository,
  private val reductionReasonRepository: ReductionReasonRepository,
  private val personManagerRepository: PersonManagerRepository,
  private val pduRepository: PduRepository,
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
        AuditMessage(operationId = UUID.randomUUID().toString(), who = loggedInUser, details = objectMapper.writeValueAsString(auditData))
      )
    )
    sqsClient.sendMessage(sendMessage)
  }

  fun publishReductionsStatusUpdatesToAuditQueue(recordIds: List<Long>, reductionStatusToAuditAction: String, loggedInUser: String = "system worker", outOfDateReductions: OutOfDateReductions) {

    // val reductionsDetails = recordIds.map { getDetailsForReduction(it, outOfDateReductions) }

    val reductionsAuditData = ReductionsAuditData(
      recordIds,
      reductionStatusToAuditAction
    )

    val sendMessage = SendMessageRequest(
      hmppsAuditQueueUrl,
      objectMapper.writeValueAsString(
        AuditMessage(operationId = UUID.randomUUID().toString(), what = "REDUCTION_UPDATE", who = loggedInUser, details = objectMapper.writeValueAsString(reductionsAuditData))
      )
    )
    sqsClient.sendMessage(sendMessage)
  }

  private fun getDetailsForReduction(reductionId: Long, outOfDateReductions: OutOfDateReductions): ReductionsDetails {

    val outOfDateReductionEntity = outOfDateReductions.let { it.activeNowArchived + it.scheduledNowActive }
      .first { it.id == reductionId }

    return reductionsRepository.findById(reductionId).map {
      ReductionsDetails(
        reductionReasonRepository.findById(outOfDateReductionEntity.reductionReasonId).get().reasonShortName,
        reductionReasonRepository.findById(it.reductionReasonId).get().reasonShortName,
        reductionsRepository.findById(outOfDateReductionEntity.reductionReasonId).get().hours,
        reductionsRepository.findById(it.reductionReasonId).get().hours,
        reductionReasonRepository.findById(outOfDateReductionEntity.reductionReasonId).get().reason,
        reductionReasonRepository.findById(it.reductionReasonId).get().reason,
        reductionsRepository.findById(outOfDateReductionEntity.reductionReasonId).get().effectiveFrom,
        reductionsRepository.findById(it.reductionReasonId).get().effectiveFrom,
        reductionsRepository.findById(outOfDateReductionEntity.reductionReasonId).get().effectiveTo,
        reductionsRepository.findById(it.reductionReasonId).get().effectiveTo,
        reductionsRepository.findById(outOfDateReductionEntity.reductionReasonId).get().status,
        reductionsRepository.findById(it.reductionReasonId).get().status,
        reductionsRepository.findById(it.reductionReasonId).get().workloadOwner,
        personManagerRepository.findById(reductionsRepository.findById(it.reductionReasonId).get().workloadOwner.id).get().teamCode,
        personManagerRepository.findById(reductionsRepository.findById(it.reductionReasonId).get().workloadOwner.id).get().providerCode,
        pduRepository.findByCode(
          personManagerRepository.findById(reductionsRepository.findById(it.reductionReasonId).get().workloadOwner.id).get().providerCode
        )?.code
      )
    }.get()
  }
}

data class AuditMessage(val operationId: String, val what: String = "CASE_ALLOCATED", val `when`: Instant = Instant.now(), val who: String, val service: String = "hmpps-workload", val details: String)
data class AuditData(
  val crn: String,
  val eventId: BigInteger,
  val requirementIds: List<BigInteger>
)

data class ReductionsAuditData(
  val recordIds: List<Long>,
  val reductionStatus: String,
)

data class ReductionsDetails(
  val previousReason: String?,
  val newReason: String?,
  val previousHours: BigDecimal?,
  val newHours: BigDecimal?,
  val previousAdditionalNotes: String?,
  val newAdditionalNotes: String?,
  val previousEffectiveFrom: ZonedDateTime?,
  val newEffectiveFrom: ZonedDateTime?,
  val previousEffectiveTo: ZonedDateTime?,
  val newEffectiveTo: ZonedDateTime?,
  val previousStatus: ReductionStatus?,
  val newStatus: ReductionStatus?,
  val offenderManagerName: WMTWorkloadOwnerEntity?,
  val team: String?,
  val pdu: String?,
  val region: String?
)
