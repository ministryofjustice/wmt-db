package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.math.BigDecimal
import java.math.BigInteger

interface CapacityCalculator {

  fun calculate(totalPoints: BigInteger, availablePoints: BigInteger): BigDecimal

  fun calculateAvailablePoints(availablePoints: BigDecimal, currentHours: BigDecimal, reductionHours: BigDecimal, defaultContractedHoursForGrade: BigDecimal): BigInteger
}
