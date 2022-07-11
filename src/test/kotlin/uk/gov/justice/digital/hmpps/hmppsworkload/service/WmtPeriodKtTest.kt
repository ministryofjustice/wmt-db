package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class WmtPeriodKtTest {

/*
Monday 11 July
Tuesday 12 July
Wednesday 13 July
Thursday 14 July
Friday 15 July
Saturday 16 July
Sunday 17 July
 */

  @Test
  fun `Monday time period - 10 July`() {
    assertEquals("2022-07-10 18:30 to 2022-07-11 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 10, 19, 45)))
  }

  @Test
  fun `Monday time period - 11 July`() {
    assertEquals("2022-07-10 18:30 to 2022-07-11 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 11, 9, 45)))
  }

  @Test
  fun `Tuesday time period  - 12 July`() {
    assertEquals("2022-07-11 18:30 to 2022-07-12 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 11, 19, 45)))
  }

  @Test
  fun `Wednesday time period - 13 July`() {
    assertEquals("2022-07-12 18:30 to 2022-07-13 19:30", getWmtPeriod(LocalDateTime.of(2022, 7, 12, 19, 45)))
  }

  @Test
  fun `Wednesday time period at 715 - 13 July`() {
    assertEquals("2022-07-12 18:30 to 2022-07-13 19:30", getWmtPeriod(LocalDateTime.of(2022, 7, 13, 19, 15)))
  }

  @Test
  fun `Thursday time period - 14 July`() {
    assertEquals("2022-07-13 19:30 to 2022-07-14 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 13, 19, 45)))
  }

  @Test
  fun `Friday time period - 15 July`() {
    assertEquals("2022-07-14 18:30 to 2022-07-15 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 14, 19, 45)))
  }

  @Test
  fun `Saturday time period`() {
    assertEquals("2022-07-15 18:30 to 2022-07-16 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 15, 19, 45)))
  }

  @Test
  fun `Sunday time period`() {
    assertEquals("2022-07-16 18:30 to 2022-07-17 18:30", getWmtPeriod(LocalDateTime.of(2022, 7, 16, 19, 45)))
  }
}
