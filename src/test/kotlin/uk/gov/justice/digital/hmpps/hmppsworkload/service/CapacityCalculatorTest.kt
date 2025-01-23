package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class CapacityCalculatorTest {
  private val availablePoints = 2176.0
  private var currentHours = 37.0
  private val reductionHours = 1.8
  private val defaultContractedHoursForGrade = 37.0

  @Test
  fun `can calculate available points`() {
    Assertions.assertEquals(
      BigInteger.valueOf(2070),
      calculateAvailableHours(availablePoints, defaultContractedHoursForGrade, currentHours, reductionHours),
    )
  }

  @Test
  fun `can calculate available points with different current hours`() {
    currentHours = 30.0

    Assertions.assertEquals(
      BigInteger.valueOf(1658),
      calculateAvailableHours(availablePoints, defaultContractedHoursForGrade, currentHours, reductionHours),
    )
  }

  private fun calculateAvailableHours(
    availablePoints: Double,
    defaultContractedHoursForGrade: Double,
    currentHours: Double,
    reductionHours: Double,
  ): BigInteger = calculateAvailablePoints(
    BigDecimal.valueOf(availablePoints),
    BigDecimal.valueOf(defaultContractedHoursForGrade),
    BigDecimal.valueOf(currentHours) - BigDecimal.valueOf(reductionHours),
  )
}
