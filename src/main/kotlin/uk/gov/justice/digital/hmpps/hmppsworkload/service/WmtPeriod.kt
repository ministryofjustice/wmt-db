package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val SEVEN_THIRTY_PM = "19:30"

private const val SIX_THIRTY_PM = "18:30"

fun getWmtPeriod(now: LocalDateTime): String {
  var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  var startPeriodTime = SIX_THIRTY_PM
  var endPeriodTime = SIX_THIRTY_PM
  var startOfPeriod = now.minusDays(1).format(formatter)
  var endOfPeriod = now.format(formatter)
  val sixThirtyToday = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 18, 30)

  if (now.isAfter(sixThirtyToday)) {
    startOfPeriod = now.format(formatter)
    endOfPeriod = now.plusDays(1).format(formatter)
  }

  if (now.dayOfWeek == DayOfWeek.TUESDAY) {
    if (now.isAfter(sixThirtyToday)) {
      endPeriodTime = SEVEN_THIRTY_PM
    }
  }

  if (now.dayOfWeek == DayOfWeek.WEDNESDAY) {
    val sevenThirtyToday = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 19, 30)
    if (now.isAfter(sevenThirtyToday)) {
      startPeriodTime = SEVEN_THIRTY_PM
    } else {
      startOfPeriod = now.minusDays(1).format(formatter)
      endOfPeriod = now.format(formatter)
      endPeriodTime = SEVEN_THIRTY_PM
    }
  }

  return "$startOfPeriod $startPeriodTime to $endOfPeriod $endPeriodTime"
}
