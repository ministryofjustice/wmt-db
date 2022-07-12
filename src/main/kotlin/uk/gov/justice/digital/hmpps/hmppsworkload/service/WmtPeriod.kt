package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val SEVEN_THIRTY_PM = "19:30"
private const val SIX_THIRTY_PM = "18:30"

fun getWmtPeriod(now: LocalDateTime): String {
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  val sixThirty = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 18, 30)
  val sevenThirty = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 19, 30)
  var startPeriodTime = SIX_THIRTY_PM
  var endPeriodTime = SIX_THIRTY_PM
  var startOfPeriod = now.minusDays(1).format(formatter)
  var endOfPeriod = now.format(formatter)

  if (now.isAfter(sixThirty)) {
    startOfPeriod = now.format(formatter)
    endOfPeriod = now.plusDays(1).format(formatter)
    if (now.dayOfWeek == TUESDAY) {
      endPeriodTime = SEVEN_THIRTY_PM
    }
  }

  if (now.dayOfWeek == WEDNESDAY) {
    if (now.isAfter(sevenThirty)) {
      startPeriodTime = SEVEN_THIRTY_PM
    } else {
      startOfPeriod = now.minusDays(1).format(formatter)
      endOfPeriod = now.format(formatter)
      endPeriodTime = SEVEN_THIRTY_PM
    }
  }
  return "$startOfPeriod $startPeriodTime to $endOfPeriod $endPeriodTime"
}
