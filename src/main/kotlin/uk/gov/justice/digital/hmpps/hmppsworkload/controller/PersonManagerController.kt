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
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetPersonManager
import java.util.UUID
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class PersonManagerController(private val getPersonManager: GetPersonManager) {

  @Operation(summary = "Get Person Manager by ID")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(responseCode = "404", description = "Result Not Found")
    ]
  )
  @PreAuthorize("hasRole('ROLE_WORKLOAD_MEASUREMENT') or hasRole('ROLE_WORKLOAD_READ')")
  @GetMapping("\${person.manager.getByIdPath}")
  fun getPersonManagerById(@PathVariable(required = true) id: UUID): PersonManagerDetails =
    getPersonManager.findById(id) ?: run {
      throw EntityNotFoundException("Person Manager not found for id $id")
    }
}
