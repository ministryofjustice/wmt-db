package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import java.math.BigDecimal

data class OffenderManagerPotentialWorkload @JsonCreator constructor(
  @Schema(description = "Probation Practitioner forename", example = "John")
  val forename: String,
  @Schema(description = "Probation Practitioner surname", example = "Smith")
  val surname: String,
  @Schema(description = "Probation Practitioner Grade", example = "PO")
  val grade: String,
  @Schema(description = "Probation Practitioner capacity correct to one decimal place", example = "56.7")
  val capacity: BigDecimal,
  @Schema(description = "Offender Manager Code", example = "OM1")
  val code: String,
  @Schema(description = "Probation Practitioner potential capacity after case is allocated", example = "8.2")
  val potentialCapacity: BigDecimal,
) {
  companion object {
    fun from(offenderManagerOverview: OffenderManagerOverview): OffenderManagerPotentialWorkload {
      return OffenderManagerPotentialWorkload(
        offenderManagerOverview.forename,
        offenderManagerOverview.surname,
        offenderManagerOverview.grade,
        offenderManagerOverview.capacity,
        offenderManagerOverview.code,
        offenderManagerOverview.potentialCapacity
      )
    }
  }
}
