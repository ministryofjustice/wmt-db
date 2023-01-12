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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PractitionerWorkload
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TeamSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.WorkloadCase
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
  fun getTeamSummary(@PathVariable(required = true) teamCode: String, @RequestParam grades: List<String>?): ResponseEntity<TeamSummary> {
    val overviews = teamService.getTeamOverview(teamCode, grades)
    if (overviews != null) {
      return ResponseEntity.ok(TeamSummary(overviews))
    }
    throw EntityNotFoundException("Team not found for $teamCode")
  }

  @Operation(summary = "Retrieve Team summary by Team Code")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/team/choose-practitioner")
  fun getPractitionersSummary(@RequestParam teamCode: List<String>, @RequestParam crn: String): ResponseEntity<PractitionerWorkload> {
    val practitionerWorkload = teamService.getPractitioner(teamCode, crn)
    if (practitionerWorkload != null) {
      return ResponseEntity.ok(practitionerWorkload)
    }
    throw EntityNotFoundException("Team not found for $teamCode")
  }

  @Operation(summary = "Retrieve Team workload and case count by Team Codes")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT')")
  @GetMapping("/team/workloadcases")
  fun getTeamWorkloadAndCaseCount(@RequestParam(required = true) teams: List<String>): Flux<WorkloadCase> {
    return teamService.getWorkloadCases(teams)
  }
}
