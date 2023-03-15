package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity

data class AllocationDetailsRequest(
  private val cases: List<AllocationDetailCase>
) {
  companion object {
    fun from(eventManagers: List<EventManagerEntity>): AllocationDetailsRequest =
      AllocationDetailsRequest(eventManagers.map { AllocationDetailCase(it.crn, it.staffCode) })
  }
}

data class AllocationDetailCase(
  val crn: String,
  val staffCode: String
)
