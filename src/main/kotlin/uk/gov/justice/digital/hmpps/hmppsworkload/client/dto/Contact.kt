package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.ZonedDateTime

data class Contact @JsonCreator constructor(
  val contactStart: ZonedDateTime
)
