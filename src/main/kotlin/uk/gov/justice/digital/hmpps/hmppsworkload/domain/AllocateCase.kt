package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class AllocateCase @JsonCreator constructor(
  val crn: String
)
