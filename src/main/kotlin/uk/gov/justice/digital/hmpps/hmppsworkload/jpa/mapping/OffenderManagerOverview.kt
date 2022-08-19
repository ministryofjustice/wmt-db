package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.EventCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class OffenderManagerOverview(
  val forename: String,
  val surname: String,
  val totalCommunityCases: Int,
  val totalCustodyCases: Int,
  val availablePoints: BigInteger,
  val totalPoints: BigInteger,
  val code: String,
  val teamName: String,
  val lastUpdatedOn: LocalDateTime?,
  val workloadOwnerId: Long,
  val paroleReportsDue: BigInteger
) {
  var capacity: BigDecimal = BigDecimal.ZERO
  var potentialCapacity: BigDecimal? = null
  var nextReductionChange: ZonedDateTime? = null
  var reductionHours: BigDecimal = BigDecimal.ZERO
  var contractedHours: BigDecimal = BigDecimal.ZERO
  var tierCaseTotals: TierCaseTotals = TierCaseTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
  var caseEndDue = BigInteger.ZERO
  var releasesDue = BigInteger.ZERO
  var grade = ""
  var lastAllocatedEvent: EventCase? = null
}
