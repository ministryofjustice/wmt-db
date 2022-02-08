package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository

@Service
class JpaBasedTeamService(
  private val teamRepository: TeamRepository
) : TeamService {

  override fun getTeamOverview(teamCode: String): List<TeamOverview> =
    teamRepository.findByOverview(teamCode)
}
