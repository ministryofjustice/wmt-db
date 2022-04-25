package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class BankHoliday(
  val date: LocalDate
)

data class RegionBankHolidays(
  val events: List<BankHoliday>
)

data class BankHolidays(
  @JsonProperty("england-and-wales")
  val englandAndWales: RegionBankHolidays
)
