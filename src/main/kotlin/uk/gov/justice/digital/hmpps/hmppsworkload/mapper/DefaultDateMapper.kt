package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.BankHolidayApiClient
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class DefaultDateMapper(private val bankHolidayApiClient: BankHolidayApiClient) : DateMapper {
  override fun addBusinessDays(startDate: LocalDate, numberOfBusinessDays: Long): LocalDate {
    var result = LocalDate.from(startDate)
    for (i in 1..numberOfBusinessDays) {
      result = result.plusDays(addToWeekDay(result))
    }
    return result
  }

  private fun addToWeekDay(date: LocalDate): Long {
    var holidayAmount = 0L
    if (isHoliday(date)) {
      holidayAmount = 1L
    }
    return when (DayOfWeek.from(date)) {
      DayOfWeek.SATURDAY -> (2L + holidayAmount)
      DayOfWeek.FRIDAY -> (3L + holidayAmount)
      else -> (1L + holidayAmount)
    }
  }

  private fun isHoliday(date: LocalDate): Boolean = bankHolidayApiClient.getBankHolidays().englandAndWales.events.any { holiday -> holiday.date.isEqual(date) }
}
