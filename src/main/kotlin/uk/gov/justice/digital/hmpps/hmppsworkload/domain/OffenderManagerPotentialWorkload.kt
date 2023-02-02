package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ImpactResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Name
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffMember
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
  @Schema(description = "Name of Person on Probation")
  val name: Name,
  @Schema(description = "Name of staff member")
  val staff: StaffMember,
  val tier: Tier
) {
  companion object {
    fun from(offenderManagerOverview: OffenderManagerOverview, impactResponse: ImpactResponse, potentialCase: Case): OffenderManagerPotentialWorkload {
      return OffenderManagerPotentialWorkload(
        impactResponse.staff.name.forename,
        impactResponse.staff.name.surname,
        impactResponse.staff.getGrade(),
        offenderManagerOverview.capacity,
        impactResponse.staff.code,
        offenderManagerOverview.potentialCapacity!!,
        impactResponse.name,
        impactResponse.staff,
        potentialCase.tier
      )
    }
  }
}
