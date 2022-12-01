package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.math.BigInteger
import java.time.ZonedDateTime
import java.util.UUID

data class RequirementManagerDetails @JsonCreator constructor(
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
  @Schema(description = "requirement ID")
  val requirementId: BigInteger,
  @Schema(description = "event number")
  val eventNumber: Int?
) {
  companion object {
    fun from(requirementManagerEntity: RequirementManagerEntity): RequirementManagerDetails {
      return RequirementManagerDetails(requirementManagerEntity.uuid, requirementManagerEntity.staffId, requirementManagerEntity.staffCode, requirementManagerEntity.teamCode, requirementManagerEntity.providerCode, requirementManagerEntity.createdBy, requirementManagerEntity.createdDate!!, requirementManagerEntity.eventId, requirementManagerEntity.requirementId, requirementManagerEntity.eventNumber)
    }
  }
}
