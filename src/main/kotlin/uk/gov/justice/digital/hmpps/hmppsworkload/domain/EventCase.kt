package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import java.time.ZonedDateTime

data class EventCase constructor(
  val tier: Tier,
  val type: CaseType,
  val crn: String,
  val allocatedOn: ZonedDateTime
)
