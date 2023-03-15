package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

data class PersonSummary @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val type: CaseType,
)
