package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class OffenderManagerOverview(
  val forename: String,
  val surname: String,
  var grade: String,
  val totalCommunityCases: BigDecimal,
  val totalCustodyCases: BigDecimal,
  val availablePoints: BigInteger,
  val totalPoints: BigInteger,
  val code: String,
  val teamName: String,
  val reductionHours: BigDecimal,
  val contractedHours: BigDecimal,
  val lastUpdatedOn: LocalDateTime?,
  val workloadOwnerId: Long,
  val paroleReportsDue: BigInteger
) {
  var capacity: BigDecimal = BigDecimal.ZERO
  var potentialCapacity: BigDecimal? = null
  var nextReductionChange: ZonedDateTime? = null
  var tierCaseTotals: TierCaseTotals = TierCaseTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
  var caseEndDue = BigInteger.ZERO
}
