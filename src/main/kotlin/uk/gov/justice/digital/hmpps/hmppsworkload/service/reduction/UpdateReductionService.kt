package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SqsSuccessPublisher
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.RequestStaffCalculationService
import java.time.ZonedDateTime

@Service
class UpdateReductionService(
  private val reductionsRepository: ReductionsRepository,
  private val sqsSuccessPublisher: SqsSuccessPublisher,
  private val requestStaffCalculationService: RequestStaffCalculationService,
) {

  @Transactional
  fun updateOutOfDateReductionStatus() {
    val outOfDateReductions = findOutOfDateReductions()

    outOfDateReductions.activeNowArchived
      .onEach { it.status = ReductionStatus.ARCHIVED }
      .let { reductionsRepository.saveAll(it).forEach { sqsSuccessPublisher.auditReduction(it, "REDUCTION_ENDED") } }
    outOfDateReductions.scheduledNowActive
      .onEach { it.status = ReductionStatus.ACTIVE }
      .let { reductionsRepository.saveAll(it).forEach { sqsSuccessPublisher.auditReduction(it, "REDUCTION_STARTED") } }

    getDistinctStaffChanged(outOfDateReductions).forEach { workloadOwner ->
      requestStaffCalculationService.requestStaffCalculation(workloadOwner)
    }

    sqsSuccessPublisher.outOfDateReductionsProcessed()
  }

  private fun getDistinctStaffChanged(outOfDateReductions: OutOfDateReductions): Set<WMTWorkloadOwnerEntity> = (outOfDateReductions.activeNowArchived + outOfDateReductions.scheduledNowActive)
    .map { it.workloadOwner }
    .toSet()

  private fun findOutOfDateReductions(): OutOfDateReductions = OutOfDateReductions(
    reductionsRepository.findByEffectiveFromBeforeAndEffectiveToAfterAndStatus(ZonedDateTime.now(), ZonedDateTime.now(), ReductionStatus.SCHEDULED),
    reductionsRepository.findByEffectiveToBeforeAndStatus(ZonedDateTime.now(), ReductionStatus.ACTIVE),
  )
}
