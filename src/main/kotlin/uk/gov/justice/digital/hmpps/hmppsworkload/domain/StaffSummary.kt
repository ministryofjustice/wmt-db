package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import java.math.BigInteger

data class StaffSummary @JsonCreator constructor(
  @Schema(description = "Probation Practitioner forename", example = "John")
  val forename: String,
  @Schema(description = "Probation Practitioner surname", example = "Smith")
  val surname: String,
  @Schema(description = "Probation Practitioner Email", example = "PO")
  val email: String?,
  @Schema(description = "Probation Practitioner ID")
  val id: BigInteger
) {
  companion object {
    fun from(staff: Staff): StaffSummary {
      return StaffSummary(
        staff.staff.forenames,
        staff.staff.surname,
        staff.email,
        staff.staffIdentifier
      )
    }
  }
}
