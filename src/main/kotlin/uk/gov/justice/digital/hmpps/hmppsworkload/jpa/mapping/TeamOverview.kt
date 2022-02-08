package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

data class TeamOverview(
  val forename: String,
  val surname: String,
  val grade: String,
  val totalCommunityCases: Long,
  val totalCustodyCases: Long,
  val availablePoints: Long,
  val totalPoints: Long
)
