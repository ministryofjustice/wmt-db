package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview

interface TeamService {

  fun getTeamOverview(teamCode: String): List<OffenderManagerOverview>?
}
