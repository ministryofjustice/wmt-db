package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTWorkloadOwnerRepository
import java.math.BigDecimal
import java.time.ZonedDateTime

@Service
class GetReductionService(private val reductionsRepository: ReductionsRepository, private val workloadOwnerRepository: WMTWorkloadOwnerRepository) {

  private val excludeStatuses = listOf(
    ReductionStatus.ARCHIVED, ReductionStatus.DELETED
  )

  fun findNextReductionChange(staffCode: String, teamCode: String): ZonedDateTime? = workloadOwnerRepository.findFirstByOffenderManagerCodeAndTeamCodeOrderByIdDesc(staffCode, teamCode)?.let { workloadOwner ->
    reductionsRepository.findByWorkloadOwnerIdAndEffectiveFromGreaterThanOrEffectiveToGreaterThanAndStatusNotIn(
      workloadOwner.id!!, ZonedDateTime.now(), ZonedDateTime.now(), excludeStatuses
    )
      .flatMap { reduction ->
        listOf(
          reduction.effectiveFrom,
          reduction.effectiveTo
        )
      }.filter { date -> !date.isBefore(ZonedDateTime.now()) }
      .minByOrNull { it }
  }

  fun findReductionHours(staffCode: String, teamCode: String): BigDecimal = (
    workloadOwnerRepository.findFirstByOffenderManagerCodeAndTeamCodeOrderByIdDesc(staffCode, teamCode)?.let { workloadOwner ->
      reductionsRepository.findByWorkloadOwnerIdAndEffectiveFromLessThanAndEffectiveToGreaterThanAndStatusNotIn(
        workloadOwner.id!!, ZonedDateTime.now(), ZonedDateTime.now(), excludeStatuses
      )
        .map { it.hours }
        .fold(BigDecimal.ZERO) { first, second -> first.add(second) }
    } ?: BigDecimal.ZERO
    ).stripTrailingZeros()
}
