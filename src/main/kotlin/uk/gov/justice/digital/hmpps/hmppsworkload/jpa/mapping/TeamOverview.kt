package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

import java.math.BigDecimal
import java.math.BigInteger

data class TeamOverview(
  val totalCommunityCases: BigDecimal,
  val totalCustodyCases: BigDecimal,
  val availablePoints: BigInteger,
  val totalPoints: BigInteger,
  val code: String,
)
