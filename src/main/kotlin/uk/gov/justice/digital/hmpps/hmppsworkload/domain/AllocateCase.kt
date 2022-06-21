package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class AllocateCase @JsonCreator constructor(
  val crn: String,
  val eventId: BigInteger,
  val instructions: String = "",
  val emailTo: List<String>? = null
)
