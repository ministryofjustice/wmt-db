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
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.MissingStaffError
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffSummary
import java.math.BigInteger
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class StaffController(private val communityApiClient: CommunityApiClient) {

  @Operation(summary = "Get Staff by ID")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/staff/{staffId}")
  fun getStaffById(@PathVariable(required = true) staffId: BigInteger): Mono<StaffSummary> =
    communityApiClient.getStaffById(staffId)
      .onErrorMap { ex ->
        when (ex) {
          is MissingStaffError -> EntityNotFoundException("staff not found for $staffId")
          else -> ex
        }
      }
      .map { staff -> StaffSummary.from(staff) }

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
    communityApiClient.getStaffByCode(staffCode)
      .onErrorMap { ex ->
        when (ex) {
          is MissingStaffError -> EntityNotFoundException("staff not found for $staffCode")
          else -> ex
        }
      }
      .map { staff -> StaffSummary.from(staff) }
}
