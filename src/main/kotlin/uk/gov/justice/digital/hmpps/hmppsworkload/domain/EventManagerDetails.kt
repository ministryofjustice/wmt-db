package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.math.BigInteger
import java.time.ZonedDateTime
import java.util.UUID

data class EventManagerDetails @JsonCreator constructor(
  @Schema(description = "Probation Practitioner ID")
  val id: UUID,
  @Schema(description = "Staff Identifier")
  val staffId: BigInteger,
  @Schema(description = "Staff Code")
  val staffCode: String,
  @Schema(description = "Team Code")
  val teamCode: String,
  @Schema(description = "Provider Code")
  val providerCode: String,
  @Schema(description = "Username of who created")
  val createdBy: String,
  @Schema(description = "Created date")
  val createdDate: ZonedDateTime,
  @Schema(description = "event ID")
  val eventId: BigInteger,
  @Schema(description = "event Number")
  val eventNumber: Int?
) {
  companion object {
    fun from(eventManagerEntity: EventManagerEntity): EventManagerDetails {
      return EventManagerDetails(eventManagerEntity.uuid, eventManagerEntity.staffId, eventManagerEntity.staffCode, eventManagerEntity.teamCode, eventManagerEntity.providerCode, eventManagerEntity.createdBy, eventManagerEntity.createdDate!!, eventManagerEntity.eventId, eventManagerEntity.eventNumber)
    }
  }
}
