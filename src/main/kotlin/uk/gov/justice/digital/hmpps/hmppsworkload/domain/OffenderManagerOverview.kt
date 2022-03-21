package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class OffenderManagerOverview @JsonCreator constructor(
  @Schema(description = "Probation Practitioner forename", example = "John")
  val forename: String,
  @Schema(description = "Probation Practitioner surname", example = "Smith")
  val surname: String,
  @Schema(description = "Probation Practitioner Grade", example = "PO")
  val grade: String,
  @Schema(description = "Probation Practitioner capacity as a decimal", example = "0.5")
  val capacity: BigDecimal,
  @Schema(description = "Offender Manager Code", example = "OM1")
  val code: String,
  @Schema(description = "Team Name", example = "Team Name")
  val teamName: String,
  @Schema(description = "Probation Practitioner total cases", example = "25")
  val totalCases: BigDecimal,
  @Schema(description = "Probation Practitioner Contracted hours per week", example = "37")
  val weeklyHours: BigDecimal,
  @Schema(description = "Probation Practitioner total Reduction hours per week", example = "10")
  val totalReductionHours: BigDecimal,
  @Schema(description = "Probation Practitioner total Points available", example = "2176")
  val pointsAvailable: BigInteger,
  @Schema(description = "Probation Practitioner total Points used", example = "1567")
  val pointsUsed: BigInteger,
  @Schema(description = "Probation Practitioner total Points remaining", example = "609")
  val pointsRemaining: BigInteger,
  @Schema(description = "Last time the Capacity was updated", example = "2013-11-03T09:00:00")
  val lastUpdatedOn: LocalDateTime?,
  @Schema(description = "Next time the reduction total will change", example = "2013-11-03T09:00:00")
  val nextReductionChange: ZonedDateTime?,
) {
  companion object {
    fun from(offenderManagerOverview: OffenderManagerOverview): uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerOverview {
      return OffenderManagerOverview(
        offenderManagerOverview.forename,
        offenderManagerOverview.surname,
        offenderManagerOverview.grade,
        offenderManagerOverview.capacity,
        offenderManagerOverview.code,
        offenderManagerOverview.teamName,
        offenderManagerOverview.totalCommunityCases.plus(offenderManagerOverview.totalCustodyCases),
        offenderManagerOverview.contractedHours,
        offenderManagerOverview.reductionHours,
        offenderManagerOverview.availablePoints,
        offenderManagerOverview.totalPoints,
        offenderManagerOverview.availablePoints.minus(offenderManagerOverview.totalPoints),
        offenderManagerOverview.lastUpdatedOn,
        offenderManagerOverview.nextReductionChange
      )
    }
  }
}
