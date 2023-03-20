package uk.gov.justice.digital.hmpps.hmppsworkload.domain.event

import java.util.UUID

data class HmppsAllocationMessage(
  val allocationId: UUID,
)
