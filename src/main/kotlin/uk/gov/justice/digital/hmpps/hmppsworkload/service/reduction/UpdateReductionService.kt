package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.AuditService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SuccessUpdater
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class UpdateReductionService(
  private val reductionsRepository: ReductionsRepository,
  private val auditService: AuditService,
  private val successUpdater: SuccessUpdater
) {

  @Transactional
  fun updateOutOfDateReductionStatus() {
    val outOfDateReductions = findOutOfDateReductions()
    val reductionStatusToAuditAction = mapOf(ReductionStatus.ACTIVE to "REDUCTION_STARTED", ReductionStatus.ARCHIVED to "REDUCTION_ENDED")

    outOfDateReductions.activeNowArchived
      .onEach { it.status = ReductionStatus.ARCHIVED }
      .let { reductionsRepository.saveAll(it) }
      .let { reductionEntities -> auditService.publishReductionsStatusUpdatesToAuditQueue(reductionEntities.map { it.id!! }, reductionStatusToAuditAction[ReductionStatus.ARCHIVED]!!, outOfDateReductions = outOfDateReductions) }
    outOfDateReductions.scheduledNowActive
      .onEach { it.status = ReductionStatus.ACTIVE }
      .let { reductionsRepository.saveAll(it) }
      .let { reductionEntities -> auditService.publishReductionsStatusUpdatesToAuditQueue(reductionEntities.map { it.id!! }, reductionStatusToAuditAction[ReductionStatus.ACTIVE]!!, outOfDateReductions = outOfDateReductions) }

    successUpdater.outOfDateReductionsProcessed()
  }

  fun findOutOfDateReductions(): OutOfDateReductions = OutOfDateReductions(
    reductionsRepository.findByEffectiveFromBeforeAndEffectiveToAfterAndStatus(ZonedDateTime.now(), ZonedDateTime.now(), ReductionStatus.SCHEDULED),
    reductionsRepository.findByEffectiveToBeforeAndStatus(ZonedDateTime.now(), ReductionStatus.ACTIVE)
  )
}
