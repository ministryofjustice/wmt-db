package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.time.ZonedDateTime
import java.util.UUID

data class EventManagerDetails @JsonCreator constructor(
  @Schema(description = "Probation Practitioner ID")
  val id: UUID,
  @Schema(description = "Staff Code")
  val staffCode: String,
  @Schema(description = "Team Code")
  val teamCode: String,
  @Schema(description = "Created date")
  val createdDate: ZonedDateTime,
  @Schema(description = "event Number")
  val eventNumber: Int?,
) {
  companion object {
    fun from(eventManagerEntity: EventManagerEntity): EventManagerDetails {
      return EventManagerDetails(eventManagerEntity.uuid, eventManagerEntity.staffCode, eventManagerEntity.teamCode, eventManagerEntity.createdDate!!, eventManagerEntity.eventNumber)
    }
  }
}
