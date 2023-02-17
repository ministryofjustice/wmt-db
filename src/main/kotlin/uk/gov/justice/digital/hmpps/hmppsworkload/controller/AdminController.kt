package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsworkload.migrate.MigrateService

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class AdminController(private val migrateService: MigrateService) {

  @Operation(summary = "Migrate event Id to event Number")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK")
    ]
  )
  @GetMapping("/admin/migrate-event-identifiers")
  fun migrateEventIdentifiers() {
    migrateService.migrateEventIdentifiers()
  }
}
