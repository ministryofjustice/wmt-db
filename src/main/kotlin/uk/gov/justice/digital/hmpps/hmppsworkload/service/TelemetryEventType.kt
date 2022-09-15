package uk.gov.justice.digital.hmpps.hmppsworkload.service

enum class TelemetryEventType(val eventName: String) {
  PERSON_MANAGER_ALLOCATED("PersonManagerAllocated"), EVENT_MANAGER_ALLOCATED("EventManagerAllocated"), REQUIREMENT_MANAGER_ALLOCATED("RequirementManagerAllocated"), STAFF_GRADE_TIER_ALLOCATED("StaffGradeTierAllocated")
}
