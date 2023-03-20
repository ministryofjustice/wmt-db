package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import java.math.BigDecimal

data class TierCaseTotals(
  val A: BigDecimal,
  val B: BigDecimal,
  val C: BigDecimal,
  val D: BigDecimal,
  val untiered: BigDecimal,
)
