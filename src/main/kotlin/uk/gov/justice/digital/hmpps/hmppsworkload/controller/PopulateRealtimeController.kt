package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppsworkload.service.PopulateRealtimeService

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class PopulateRealtimeController(private val populateRealtimeService: PopulateRealtimeService) {

  @PostMapping("/cases/populate/name")
  fun uploadCases(): ResponseEntity<Void> {
    populateRealtimeService.populateEventsFromCaseDetails()
    return ResponseEntity.ok().build()
  }
}
