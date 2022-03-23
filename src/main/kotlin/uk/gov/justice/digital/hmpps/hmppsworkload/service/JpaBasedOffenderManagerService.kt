package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PotentialCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class JpaBasedOffenderManagerService(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val capacityCalculator: CapacityCalculator,
  private val caseCalculator: CaseCalculator,
  private val reductionsRepository: ReductionsRepository
) : OffenderManagerService {

  override fun getPotentialWorkload(
    teamCode: String,
    offenderManagerCode: String,
    potentialCase: PotentialCase
  ): OffenderManagerOverview? =
    offenderManagerRepository.findByOverview(teamCode, offenderManagerCode)?.let {
      it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
      it.potentialCapacity = capacityCalculator.calculate(it.totalPoints.plus(caseCalculator.getPointsForCase(potentialCase)), it.availablePoints)
      return it
    }

  override fun getOverview(teamCode: String, offenderManagerCode: String): OffenderManagerOverview? = offenderManagerRepository.findByOverview(teamCode, offenderManagerCode)?.let {
    it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
    reductionsRepository.findByStatusIsInAndWorkloadOwnerIdIs(listOf(ReductionStatus.ACTIVE, ReductionStatus.SCHEDULED), it.workloadOwnerId).let { reductions ->
      reductions.flatMap { reduction -> listOf(reduction.effectiveFrom, reduction.effectiveTo ?: ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())) }
        .filter { date -> !date.isBefore(ZonedDateTime.now()) }
        .minByOrNull { date -> date }
        ?.let { nextReductionChange -> it.nextReductionChange = nextReductionChange }
    }
    offenderManagerRepository.findByCaseloadTotals(it.workloadOwnerId).let { totals ->
      it.tierCaseTotals = totals.map { total -> TierCaseTotals(total.getATotal(), total.getBTotal(), total.getCTotal(), total.getDTotal(), total.untiered) }
        .reduce { first, second -> TierCaseTotals(first.A.add(second.A), first.B.add(second.B), first.C.add(second.C), first.D.add(second.D), first.untiered.add(second.untiered)) }
    }
    return it
  }
}
