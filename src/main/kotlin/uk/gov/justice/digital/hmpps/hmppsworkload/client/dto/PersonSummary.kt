package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class PersonSummary @JsonCreator constructor(
  val firstName: String,
  val surname: String,
)
