package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ActiveCase
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffActiveCases
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity

data class OffenderManagerCases @JsonCreator constructor(
  @Schema(description = "Probation Practitioner forename", example = "John")
  val forename: String,
  @Schema(description = "Probation Practitioner surname", example = "Smith")
  val surname: String,
  @Schema(description = "Probation Practitioner Grade", example = "PO")
  val grade: String,
  @Schema(description = "Offender Manager Code", example = "OM1")
  val code: String,
  val activeCases: List<OffenderManagerActiveCase>,
  @Schema(description = "Email", example = "offender.manager@justice.gov.uk")
  val email: String?,
) {
  companion object {
    fun from(staffActiveCases: StaffActiveCases, offenderDetails: Map<String, CaseDetailsEntity>): OffenderManagerCases {
      return OffenderManagerCases(staffActiveCases.name.forename, staffActiveCases.name.surname, staffActiveCases.getGrade(), staffActiveCases.code, staffActiveCases.cases.map { OffenderManagerActiveCase.from(it, offenderDetails[it.crn]!!) }, staffActiveCases.email)
    }
  }
}

data class OffenderManagerActiveCase(
  @Schema(description = "CRN", example = "CRN111111")
  val crn: String,
  @Schema(description = "Tier", example = "B1")
  val tier: String,
  @Schema(description = "Case Category", example = "LICENSE")
  val caseCategory: String,
  @Schema(description = "Case forename", example = "Sally")
  val forename: String?,
  @Schema(description = "Case surname", example = "Smith")
  val surname: String?
) {
  companion object {
    fun from(activeCase: ActiveCase, caseDetails: CaseDetailsEntity): OffenderManagerActiveCase {
      return OffenderManagerActiveCase(activeCase.crn, caseDetails.tier.name, activeCase.type, activeCase.name.forename, activeCase.name.surname)
    }
  }
}
