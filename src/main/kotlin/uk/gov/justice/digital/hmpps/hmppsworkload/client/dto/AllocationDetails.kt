package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

class AllocationDetails(val cases: List<AllocationDetail>)

data class AllocationDetail(
  val crn: String,
  val name: Name,
  val staff: StaffMember
)
