package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

import java.math.BigDecimal
import java.math.BigInteger

data class TeamOverview(
  val forename: String,
  val surname: String,
  val totalCommunityCases: BigDecimal,
  val totalCustodyCases: BigDecimal,
  val availablePoints: BigInteger,
  val totalPoints: BigInteger,
  val code: String,
) {
  var capacity: BigDecimal = BigDecimal.ZERO
  var staffId: BigInteger = BigInteger.valueOf(-1)
  var casesInLastWeek: BigInteger = BigInteger.ZERO
  var grade = ""
}
