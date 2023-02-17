package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.time.LocalDateTime

private const val CRN = "crn"

private const val TEAM_CODE = "teamCode"

private const val PROVIDER_CODE = "providerCode"

private const val STAFF_CODE = "staffCode"

private const val EVENT_NUMBER = "eventNumber"

@Component
class TelemetryService(@Autowired private val telemetryClient: TelemetryClient) {

  fun trackPersonManagerAllocated(personManagerEntity: PersonManagerEntity) {
    trackEvent(
      TelemetryEventType.PERSON_MANAGER_ALLOCATED,
      mapOf(
        CRN to personManagerEntity.crn,
        TEAM_CODE to personManagerEntity.teamCode,
        STAFF_CODE to personManagerEntity.staffCode,
        "wmtPeriod" to getWmtPeriod(LocalDateTime.now())
      )
    )
  }

  fun trackEventManagerAllocated(eventManagerEntity: EventManagerEntity) {
    trackEvent(
      TelemetryEventType.EVENT_MANAGER_ALLOCATED,
      mapOf(
        CRN to eventManagerEntity.crn,
        TEAM_CODE to eventManagerEntity.teamCode,
        STAFF_CODE to eventManagerEntity.staffCode,
        EVENT_NUMBER to eventManagerEntity.eventNumber.toString(10)
      )
    )
  }

  fun trackRequirementManagerAllocated(requirementManagerEntity: RequirementManagerEntity) {
    trackEvent(
      TelemetryEventType.REQUIREMENT_MANAGER_ALLOCATED,
      mapOf(
        CRN to requirementManagerEntity.crn,
        TEAM_CODE to requirementManagerEntity.teamCode,
        STAFF_CODE to requirementManagerEntity.staffCode,
        EVENT_NUMBER to requirementManagerEntity.eventNumber.toString(10),
        "requirementId" to requirementManagerEntity.requirementId.toString(10)
      )
    )
  }

  fun trackStaffGradeToTierAllocated(caseDetailsEntity: CaseDetailsEntity?, deliusStaff: DeliusStaff, teamCode: String) {
    trackEvent(
      TelemetryEventType.STAFF_GRADE_TIER_ALLOCATED,
      mapOf(
        TEAM_CODE to teamCode,
        PROVIDER_CODE to deliusStaff.probationArea!!.code,
        "staffGrade" to deliusStaff.grade,
        "tier" to caseDetailsEntity?.tier?.name
      )
    )
  }
  private fun trackEvent(eventType: TelemetryEventType, customDimensions: Map<String, String?>) {
    telemetryClient.trackEvent(eventType.eventName, customDimensions, null)
  }
}
