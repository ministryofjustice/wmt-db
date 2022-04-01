package uk.gov.justice.digital.hmpps.hmppsworkload.domain.event

import java.util.UUID

data class HmppsPersonAllocationMessage(
  val allocationId: UUID,
  val crn: String
)
