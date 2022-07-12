package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.time.LocalDateTime

private const val CRN = "crn"

private const val TEAM_CODE = "teamCode"

private const val PROVIDER_CODE = "providerCode"

private const val STAFF_ID = "staffId"

private const val EVENT_ID = "eventId"

@Component
class TelemetryService(@Autowired private val telemetryClient: TelemetryClient) {

  fun trackPersonManagerAllocated(personManagerEntity: PersonManagerEntity) {
    trackEvent(
      TelemetryEventType.PERSON_MANAGER_ALLOCATED,
      mapOf(
        CRN to personManagerEntity.crn,
        TEAM_CODE to personManagerEntity.teamCode,
        PROVIDER_CODE to personManagerEntity.providerCode,
        STAFF_ID to personManagerEntity.staffId.toString(10),
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
        PROVIDER_CODE to eventManagerEntity.providerCode,
        STAFF_ID to eventManagerEntity.staffId.toString(10),
        EVENT_ID to eventManagerEntity.eventId.toString(10)
      )
    )
  }

  fun trackRequirementManagerAllocated(requirementManagerEntity: RequirementManagerEntity) {
    trackEvent(
      TelemetryEventType.REQUIREMENT_MANAGER_ALLOCATED,
      mapOf(
        CRN to requirementManagerEntity.crn,
        TEAM_CODE to requirementManagerEntity.teamCode,
        PROVIDER_CODE to requirementManagerEntity.providerCode,
        STAFF_ID to requirementManagerEntity.staffId.toString(10),
        EVENT_ID to requirementManagerEntity.eventId.toString(10),
        "requirementId" to requirementManagerEntity.requirementId.toString(10)
      )
    )
  }
  private fun trackEvent(eventType: TelemetryEventType, customDimensions: Map<String, String?>) {
    telemetryClient.trackEvent(eventType.eventName, customDimensions, null)
  }
}
