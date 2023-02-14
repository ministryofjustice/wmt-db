package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OfficerView

data class StaffSummary @JsonCreator constructor(
  @Schema(description = "Probation Practitioner forename", example = "John")
  val forename: String,
  @Schema(description = "Probation Practitioner surname", example = "Smith")
  val surname: String,
  @Schema(description = "Probation Practitioner Email", example = "PO")
  val email: String?
) {
  companion object {
    fun from(officerView: OfficerView): StaffSummary {
      return StaffSummary(
        officerView.name.forename,
        officerView.name.surname,
        officerView.email
      )
    }
  }
}
