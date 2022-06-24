package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import java.math.BigDecimal
import java.time.ZonedDateTime

@Service
class GetReductionService(private val reductionsRepository: ReductionsRepository) {

  private val excludeStatuses = listOf(
    ReductionStatus.ARCHIVED, ReductionStatus.DELETED
  )

  fun findNextReductionChange(workloadOwnerId: Long): ZonedDateTime? = reductionsRepository.findByWorkloadOwnerIdAndEffectiveFromGreaterThanOrEffectiveToGreaterThanAndStatusNotIn(
    workloadOwnerId, ZonedDateTime.now(), ZonedDateTime.now(), excludeStatuses
  )
    .flatMap { reduction ->
      listOf(
        reduction.effectiveFrom,
        reduction.effectiveTo
      )
    }.filter { date -> !date.isBefore(ZonedDateTime.now()) }
    .minByOrNull { it }

  fun findReductionHours(workloadOwnerId: Long): BigDecimal = reductionsRepository.findByWorkloadOwnerIdAndEffectiveFromLessThanAndEffectiveToGreaterThanAndStatusNotIn(
    workloadOwnerId, ZonedDateTime.now(), ZonedDateTime.now(), excludeStatuses
  )
    .map { it.hours }
    .fold(BigDecimal.ZERO) { first, second -> first.add(second) }.stripTrailingZeros()
}
