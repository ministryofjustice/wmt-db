package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Team
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerCase

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
  val activeCases: List<OffenderManagerActiveCase>
) {
  companion object {
    fun from(staff: Staff, grade: String, team: Team, cases: List<OffenderManagerCase>): OffenderManagerCases {
      return OffenderManagerCases(staff.staff.forenames, staff.staff.surname, grade, staff.staffCode, team.description, cases.map { OffenderManagerActiveCase.from(it) })
    }
  }
}

data class OffenderManagerActiveCase(
  @Schema(description = "CRN", example = "CRN111111")
  val crn: String,
  @Schema(description = "Tier", example = "B1")
  val tier: String,
  @Schema(description = "Case Category", example = "LICENSE")
  val caseCategory: String
) {
  companion object {
    fun from(offenderManagerCase: OffenderManagerCase): OffenderManagerActiveCase {
      return OffenderManagerActiveCase(offenderManagerCase.crn, offenderManagerCase.tier, offenderManagerCase.caseCategory)
    }
  }
}
