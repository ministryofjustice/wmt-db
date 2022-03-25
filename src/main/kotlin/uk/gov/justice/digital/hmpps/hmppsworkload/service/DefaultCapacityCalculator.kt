package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@Service
class DefaultCapacityCalculator : CapacityCalculator {
  override fun calculate(totalPoints: BigInteger, availablePoints: BigInteger): BigDecimal {
    if (totalPoints != BigInteger.ZERO && availablePoints != BigInteger.ZERO) {
      return BigDecimal(totalPoints)
        .divide(BigDecimal(availablePoints), 3, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
    }
    return BigDecimal.ZERO
  }

  override fun calculateAvailablePoints(
    availablePoints: BigDecimal,
    currentHours: BigDecimal,
    reductionHours: BigDecimal,
    defaultContractedHoursForGrade: BigDecimal
  ): BigDecimal {
    if (currentHours != BigDecimal.ZERO && defaultContractedHoursForGrade != BigDecimal.ZERO) {
      val availablePointsForCurrentHours = availablePoints.multiply(currentHours.divide(defaultContractedHoursForGrade))
      val currentHoursWorked = currentHours.minus(reductionHours).divide(currentHours)
      return availablePointsForCurrentHours.multiply(currentHoursWorked)
    }
    return BigDecimal.ZERO
  }
}
