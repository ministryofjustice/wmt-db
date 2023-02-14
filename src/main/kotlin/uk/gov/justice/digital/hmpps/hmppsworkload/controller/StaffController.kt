package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffSummary
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class StaffController(private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient) {

  @Operation(summary = "Get Staff by Code")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/staff/code/{staffCode}")
  fun getStaffById(@PathVariable(required = true) staffCode: String): Mono<StaffSummary> =
    workforceAllocationsToDeliusApiClient.getOfficerView(staffCode)
      .onErrorMap { ex ->
        when (ex) {
          is WebClientResponseException.NotFound -> EntityNotFoundException("staff not found for $staffCode")
          else -> ex
        }
      }
      .map { staff -> StaffSummary.from(staff) }
}
