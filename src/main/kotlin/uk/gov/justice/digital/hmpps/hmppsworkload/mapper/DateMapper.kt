package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import java.time.LocalDate

interface DateMapper {

  fun addBusinessDays(startDate: LocalDate, numberOfBusinessDays: Long): LocalDate
}
