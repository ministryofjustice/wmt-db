package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TeamSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.service.TeamService
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class TeamController(
  private val teamService: TeamService
) {

  @Operation(summary = "Retrieve Team summary by Team Code")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/team/{teamCode}/offenderManagers")
  fun getTeamSummary(@PathVariable(required = true) teamCode: String): ResponseEntity<TeamSummary> {
    val overviews = teamService.getTeamOverview(teamCode)
    if (overviews != null) {
      return ResponseEntity.ok(TeamSummary.from(overviews))
    }
    throw EntityNotFoundException("Team not found for $teamCode")
  }
}
