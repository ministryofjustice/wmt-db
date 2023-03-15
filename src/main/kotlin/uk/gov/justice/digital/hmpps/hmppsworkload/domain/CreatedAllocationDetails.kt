package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.AllocationDetail
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Name
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffMember
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.time.ZonedDateTime

data class CreatedAllocationDetails(val cases: List<CreatedAllocationDetail>) {
  companion object {
    fun from(eventManagers: List<EventManagerEntity>, eventManagerDetails: Map<String, AllocationDetail>, caseDetails: Map<String, CaseDetailsEntity>): CreatedAllocationDetails {
      return CreatedAllocationDetails(
        eventManagers
          .filter { eventManagerDetails.containsKey(it.crn) && caseDetails.containsKey(it.crn) }
          .map { CreatedAllocationDetail.from(eventManagerDetails[it.crn]!!, caseDetails[it.crn]!!, it) }
      )
    }
  }
}

data class CreatedAllocationDetail(
  val crn: String,
  val name: Name,
  val staff: StaffMember,
  val tier: Tier,
  val allocatedOn: ZonedDateTime
) {
  companion object {
    fun from(allocationDetail: AllocationDetail, caseDetail: CaseDetailsEntity, eventManagerEntity: EventManagerEntity): CreatedAllocationDetail {
      return CreatedAllocationDetail(eventManagerEntity.crn, allocationDetail.name, allocationDetail.staff, caseDetail.tier, eventManagerEntity.createdDate!!)
    }
  }
}
