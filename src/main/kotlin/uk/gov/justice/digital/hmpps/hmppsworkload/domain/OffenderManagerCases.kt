package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Team

data class OffenderManagerCases @JsonCreator constructor(
  @Schema(description = "Probation Practitioner forename", example = "John")
  val forename: String,
  @Schema(description = "Probation Practitioner surname", example = "Smith")
  val surname: String,
  @Schema(description = "Probation Practitioner Grade", example = "PO")
  val grade: String,
  @Schema(description = "Offender Manager Code", example = "OM1")
  val code: String,
  @Schema(description = "Team Name", example = "Team Name")
  val teamName: String,
) {
  companion object {
    fun from(staff: Staff, grade: String, team: Team): OffenderManagerCases {
      return OffenderManagerCases(staff.staff.forenames, staff.staff.surname, grade, staff.staffCode, team.description)
    }
  }
}
