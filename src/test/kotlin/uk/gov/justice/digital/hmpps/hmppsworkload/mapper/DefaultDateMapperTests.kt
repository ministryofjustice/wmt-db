package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.client.BankHolidayApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.BankHoliday
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.BankHolidays
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RegionBankHolidays
import java.time.LocalDate
import java.time.Month

class DefaultDateMapperTests {

  private val holidayApiClient = mockk<BankHolidayApiClient>()
  private val dateMapper = DefaultDateMapper(holidayApiClient)

  @BeforeEach
  fun setup() {
    every { holidayApiClient.getBankHolidays() } returns generateNoBankHolidays()
  }

  @Test
  fun `must be able to add days onto a date`() {
    val startDate = LocalDate.of(2022, Month.APRIL, 25)
    val daysToAddOn = 4L
    val result = dateMapper.addBusinessDays(startDate, daysToAddOn)
    Assertions.assertTrue(result.isEqual(LocalDate.of(startDate.year, startDate.month, (startDate.dayOfMonth + daysToAddOn).toInt())))
  }

  @Test
  fun `must not include weekends when adding business days`() {
    val startDate = LocalDate.of(2022, Month.APRIL, 21)
    val daysToAddOn = 5L
    val result = dateMapper.addBusinessDays(startDate, daysToAddOn)
    Assertions.assertTrue(result.isEqual(LocalDate.of(startDate.year, startDate.month, (startDate.dayOfMonth + daysToAddOn + 2).toInt())))
  }

  @Test
  fun `must not include holidays when adding business days`() {
    val startDate = LocalDate.of(2022, Month.APRIL, 25)
    val daysToAddOn = 4L
    every { holidayApiClient.getBankHolidays() } returns generateBankHoliday(startDate.plusDays(1L))
    val result = dateMapper.addBusinessDays(startDate, daysToAddOn)
    Assertions.assertTrue(result.isEqual(startDate.plusDays(daysToAddOn + 3)))
  }

  private fun generateBankHoliday(holiday: LocalDate): BankHolidays = BankHolidays(RegionBankHolidays(listOf(BankHoliday(holiday))))

  private fun generateNoBankHolidays(): BankHolidays = BankHolidays(RegionBankHolidays(emptyList()))
}
