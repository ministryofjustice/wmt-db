package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getWmtPeriod(now: LocalDateTime): String {
  var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  var startPeriodTime = "18:30"
  var endPeriodTime = "18:30"
  var startOfPeriod = now.minusDays(1).format(formatter)
  var endOfPeriod = now.format(formatter)
  val sixThirtyToday = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 18, 30)

  if (now.isAfter(sixThirtyToday)) {
    startOfPeriod = now.format(formatter)
    endOfPeriod = now.plusDays(1).format(formatter)
  }

  if (now.dayOfWeek == DayOfWeek.TUESDAY) {
    if (now.isAfter(sixThirtyToday)) {
      endPeriodTime = "19:30"
    }
  }

  if (now.dayOfWeek == DayOfWeek.WEDNESDAY) {
    val sevenThirtyToday = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 19, 30)
    if (now.isAfter(sevenThirtyToday)) {
      startPeriodTime = "19:30"
    } else {
      startOfPeriod = now.minusDays(1).format(formatter)
      endOfPeriod = now.format(formatter)
      endPeriodTime = "19:30"
    }
  }

  return "$startOfPeriod $startPeriodTime to $endOfPeriod $endPeriodTime"
}
