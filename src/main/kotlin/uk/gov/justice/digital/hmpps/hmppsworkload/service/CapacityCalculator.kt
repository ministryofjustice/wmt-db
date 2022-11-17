package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigInteger
import java.math.RoundingMode.HALF_UP

fun calculateCapacity(totalPoints: BigInteger, availablePoints: BigInteger): BigDecimal {
  if (totalPoints != BigInteger.ZERO && availablePoints != BigInteger.ZERO) {
    return BigDecimal(totalPoints)
      .divide(BigDecimal(availablePoints), 3, HALF_UP).multiply(BigDecimal.valueOf(100))
  }
  return ZERO
}

fun calculateAvailablePoints(
  pointsForGrade: BigDecimal,
  contractedHoursForGrade: BigDecimal,
  availableHours: BigDecimal
): BigInteger {
  if (availableHours > ZERO && contractedHoursForGrade != ZERO) {
    val percentageToWork = availableHours.divide(contractedHoursForGrade, 15, HALF_UP)
    return (pointsForGrade * percentageToWork).toBigInteger()
  }
  return BigInteger.ZERO
}
