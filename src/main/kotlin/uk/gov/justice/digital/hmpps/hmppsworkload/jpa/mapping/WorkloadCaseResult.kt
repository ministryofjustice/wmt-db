package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

data class WorkloadCaseResult(
  val totalCases: Int,
  val availablePoints: Int,
  val totalPoints: Int,
  val teamCode: String
)
