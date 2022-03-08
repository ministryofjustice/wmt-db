package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository

@Service
class JpaBasedTeamService(
  private val teamRepository: TeamRepository,
  private val capacityCalculator: CapacityCalculator
) : TeamService {

  override fun getTeamOverview(teamCode: String): List<TeamOverview>? {
    var overviews: List<TeamOverview>? = null
    if (teamRepository.existsByCode(teamCode)) {
      overviews = teamRepository.findByOverview(teamCode).map {
        it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
        it
      }
    }
    return overviews
  }
}
