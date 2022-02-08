package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview

interface TeamService {

  fun getTeamOverview(teamCode: String): List<TeamOverview>?
}
