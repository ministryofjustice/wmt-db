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
  val A3_S: BigDecimal,
  val A2_S: BigDecimal,
  val A1_S: BigDecimal,
  val A0_S: BigDecimal,
  val B3_S: BigDecimal,
  val B2_S: BigDecimal,
  val B1_S: BigDecimal,
  val B0_S: BigDecimal,
  val C3_S: BigDecimal,
  val C2_S: BigDecimal,
  val C1_S: BigDecimal,
  val C0_S: BigDecimal,
  val D3_S: BigDecimal,
  val D2_S: BigDecimal,
  val D1_S: BigDecimal,
  val D0_S: BigDecimal,
) {
  fun getATotal(): BigDecimal = A3.add(A2).add(A1).add(A0)
  fun getBTotal(): BigDecimal = B3.add(B2).add(B1).add(B0)
  fun getCTotal(): BigDecimal = C3.add(C2).add(C1).add(C0)
  fun getDTotal(): BigDecimal = D3.add(D2).add(D1).add(D0)
  fun getASTotal(): BigDecimal = A3_S.add(A2_S).add(A1_S).add(A0_S)
  fun getBSTotal(): BigDecimal = B3_S.add(B2_S).add(B1_S).add(B0_S)
  fun getCSTotal(): BigDecimal = C3_S.add(C2_S).add(C1_S).add(C0_S)
  fun getDSTotal(): BigDecimal = D3_S.add(D2_S).add(D1_S).add(D0_S)
}
