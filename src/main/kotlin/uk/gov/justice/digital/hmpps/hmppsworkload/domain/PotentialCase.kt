package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class PotentialCase @JsonCreator constructor(
  val tier: String,
  val type: CaseType,
  val T2A: Boolean
)
