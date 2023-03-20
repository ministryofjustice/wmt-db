package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class ImpactResponse @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val staff: StaffMember,
)
