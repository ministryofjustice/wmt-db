package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class OffenderDetails @JsonCreator constructor(
  val firstName: String,
  val surname: String,
  val otherIds: OtherIds
)

data class OtherIds @JsonCreator constructor(
  val crn: String
)
