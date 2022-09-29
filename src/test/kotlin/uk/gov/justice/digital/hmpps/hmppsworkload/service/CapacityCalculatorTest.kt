package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class CapacityCalculatorTest {
  private val capacityCalculator = CapacityCalculator()

  @Test
  fun `can calculate available points`() {
    val availablePoints = BigDecimal.valueOf(2176)
    val currentHours = BigDecimal.valueOf(37)
    val reductionHours = BigDecimal.valueOf(1.8)
    val defaultContractedHoursForGrade = BigDecimal.valueOf(37)

    val calculatedAvailablePoints = capacityCalculator.calculateAvailablePoints(
      availablePoints,
      currentHours,
      reductionHours,
      defaultContractedHoursForGrade
    )

    Assertions.assertEquals(BigInteger.valueOf(2070), calculatedAvailablePoints)
  }

  @Test
  fun `can calculate available points with different current hours`() {
    val availablePoints = BigDecimal.valueOf(2176)
    val currentHours = BigDecimal.valueOf(30)
    val reductionHours = BigDecimal.valueOf(1.8)
    val defaultContractedHoursForGrade = BigDecimal.valueOf(37)

    val calculatedAvailablePoints = capacityCalculator.calculateAvailablePoints(
      availablePoints,
      currentHours,
      reductionHours,
      defaultContractedHoursForGrade
    )

    Assertions.assertEquals(BigInteger.valueOf(1658), calculatedAvailablePoints)
  }
}
