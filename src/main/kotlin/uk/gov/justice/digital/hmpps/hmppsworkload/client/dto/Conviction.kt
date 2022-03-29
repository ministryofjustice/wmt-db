package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class Conviction @JsonCreator constructor(
  val sentence: Sentence?,
  val custody: Custody?,
  val convictionId: Long
)

data class Custody @JsonCreator constructor(
  val status: CustodyStatus,
)

data class CustodyStatus @JsonCreator constructor(
  val code: String,
)
