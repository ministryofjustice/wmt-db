package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerPotentialWorkload
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PotentialCase
import uk.gov.justice.digital.hmpps.hmppsworkload.service.OffenderManagerService
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class OffenderManagerController(
  private val offenderManagerService: OffenderManagerService
) {

  @Operation(summary = "Retrieves capacity and potential capacity if case were to be allocated")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @PostMapping("/team/{teamCode}/offenderManagers/{offenderManagerCode}/potentialCases")
  fun getPotentialCaseCapacity(@PathVariable(required = true) teamCode: String, @PathVariable(required = true) offenderManagerCode: String, @RequestBody potentialCase: PotentialCase): OffenderManagerPotentialWorkload {
    val potentialWorkload = offenderManagerService.getPotentialWorkload(teamCode, offenderManagerCode, potentialCase)
    if (potentialWorkload != null) {
      return OffenderManagerPotentialWorkload.from(potentialWorkload)
    }
    throw EntityNotFoundException("Team $teamCode and offender manager $offenderManagerCode combination not found")
  }

  @Operation(summary = "Retrieves overview of Offender Manager")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/team/{teamCode}/offenderManagers/{offenderManagerCode}")
  fun getOverview(@PathVariable(required = true) teamCode: String, @PathVariable(required = true) offenderManagerCode: String): OffenderManagerOverview {
    return offenderManagerService.getOverview(teamCode, offenderManagerCode)?.let {
      OffenderManagerOverview.from(it)
    } ?: run {
      throw EntityNotFoundException("Team $teamCode and offender manager $offenderManagerCode combination not found")
    }
  }
}
