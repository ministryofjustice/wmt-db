package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping

import java.math.BigDecimal

data class OffenderManagerCaseloadTotals(
  val location: String,
  val untiered: BigDecimal,
  val A3: BigDecimal,
  val A2: BigDecimal,
  val A1: BigDecimal,
  val A0: BigDecimal,
  val B3: BigDecimal,
  val B2: BigDecimal,
  val B1: BigDecimal,
  val B0: BigDecimal,
  val C3: BigDecimal,
  val C2: BigDecimal,
  val C1: BigDecimal,
  val C0: BigDecimal,
  val D3: BigDecimal,
  val D2: BigDecimal,
  val D1: BigDecimal,
  val D0: BigDecimal,
) {
  fun getATotal(): BigDecimal {
    return A3.add(A2).add(A1).add(A0)
  }
  fun getBTotal(): BigDecimal {
    return B3.add(B2).add(B1).add(B0)
  }
  fun getCTotal(): BigDecimal {
    return C3.add(C2).add(C1).add(C0)
  }
  fun getDTotal(): BigDecimal {
    return D3.add(D2).add(D1).add(D0)
  }
}
