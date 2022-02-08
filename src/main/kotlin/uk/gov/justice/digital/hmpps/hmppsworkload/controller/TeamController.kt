package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TeamSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.service.TeamService

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class TeamController(
  private val teamService: TeamService
) {

  @PreAuthorize("hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/team/{teamCode}/summary")
  fun getTeamSummary(@PathVariable(required = true) teamCode: String): ResponseEntity<TeamSummary> =
    ResponseEntity.ok(TeamSummary.from(teamService.getTeamOverview(teamCode)))
}
