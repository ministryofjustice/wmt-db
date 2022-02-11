package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PotentialCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository

@Service
class JpaBasedOffenderManagerService(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val capacityCalculator: CapacityCalculator,
  private val caseCalculator: CaseCalculator
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
}
