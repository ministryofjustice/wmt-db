package uk.gov.justice.digital.hmpps.hmppsworkload.domain.event

import java.math.BigDecimal

data class StaffAvailableHours(
  val availableHours: BigDecimal,
)
