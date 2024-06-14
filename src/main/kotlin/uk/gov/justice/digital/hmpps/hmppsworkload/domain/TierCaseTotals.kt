package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import java.math.BigDecimal

data class TierCaseTotals(
  val A: BigDecimal,
  val B: BigDecimal,
  val C: BigDecimal,
  val D: BigDecimal,
  val AS: BigDecimal,
  val BS: BigDecimal,
  val CS: BigDecimal,
  val DS: BigDecimal,
  val untiered: BigDecimal,
)
