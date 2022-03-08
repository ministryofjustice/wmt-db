package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

import java.math.BigDecimal
import java.math.BigInteger

data class OffenderManagerOverview(
  val forename: String,
  val surname: String,
  val grade: String,
  val totalCommunityCases: BigDecimal,
  val totalCustodyCases: BigDecimal,
  val availablePoints: BigInteger,
  val totalPoints: BigInteger,
  val code: String,
  val teamName: String,
  val reductionHours: BigDecimal,
  val contractedHours: BigDecimal
) {
  lateinit var capacity: BigDecimal
  lateinit var potentialCapacity: BigDecimal
}
