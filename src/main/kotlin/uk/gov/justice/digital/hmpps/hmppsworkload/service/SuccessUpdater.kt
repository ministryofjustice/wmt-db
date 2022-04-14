package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.time.ZonedDateTime
import java.util.UUID

interface SuccessUpdater {

  fun updatePerson(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime)
  fun updateEvent(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime)
  fun updateRequirement(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime)
}
