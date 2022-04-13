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
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetStaffService
import java.math.BigInteger
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class StaffController(private val getStaffService: GetStaffService) {

  @Operation(summary = "Get Staff by ID")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("/staff/{staffId}")
  fun getStaffById(@PathVariable(required = true) staffId: BigInteger): StaffSummary =
    getStaffService.getStaffById(staffId)?.let { staff -> StaffSummary.from(staff) } ?: run {
      throw EntityNotFoundException("staff ID not found for $staffId")
    }
}
