package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.UUID

data class CaseAllocated @JsonCreator constructor(
  val id: UUID
)
