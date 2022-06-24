package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class GetReductionService(private val reductionsRepository: ReductionsRepository) {

  fun findNextReductionChange(workloadOwnerId: Long): ZonedDateTime? = reductionsRepository.findByStatusIsInAndWorkloadOwnerIdIs(
    listOf(
      ReductionStatus.ACTIVE, ReductionStatus.SCHEDULED
    ),
    workloadOwnerId
  ).let { reductions ->
    reductions.flatMap { reduction ->
      listOf(
        reduction.effectiveFrom,
        reduction.effectiveTo ?: ZonedDateTime.ofInstant(
          Instant.EPOCH, ZoneId.systemDefault()
        )
      )
    }
      .filter { date -> !date.isBefore(ZonedDateTime.now()) }
      .minByOrNull { it }
  }

  fun findReductionHours(workloadOwnerId: Long): BigDecimal = reductionsRepository.findByWorkloadOwnerIdAndEffectiveFromLessThanAndEffectiveToGreaterThan(workloadOwnerId, ZonedDateTime.now(), ZonedDateTime.now())
    .map { it.hours }
    .fold(BigDecimal.ZERO) { first, second -> first.add(second) }.stripTrailingZeros()
}
