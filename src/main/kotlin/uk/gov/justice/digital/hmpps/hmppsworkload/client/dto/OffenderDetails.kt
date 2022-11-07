package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

data class OffenderDetails(
  val otherIds: OtherIds
)

data class OtherIds(
  val crn: String,
  val nomsNumber: String,
)
