package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository
import java.math.BigDecimal

@Service
class JpaBasedTeamService(
  private val teamRepository: TeamRepository,
  private val capacityCalculator: CapacityCalculator
) : TeamService {

  override fun getTeamOverview(teamCode: String): List<OffenderManagerOverview>? {
    var overviews: List<OffenderManagerOverview>? = null
    if (teamRepository.existsByCode(teamCode)) {
      overviews = teamRepository.findByOverview(teamCode).map {
        it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
        it.potentialCapacity = BigDecimal.ZERO
        it
      }
    }
    return overviews
  }
}
