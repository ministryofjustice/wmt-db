package uk.gov.justice.digital.hmpps.hmppsworkload.service

import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity

@Component
class TelemetryService(@Autowired private val telemetryClient: TelemetryClient) {

  fun trackPersonManagerAllocated(personManagerEntity: PersonManagerEntity) {
    trackEvent(
      TelemetryEventType.PERSON_MANAGER_ALLOCATED,
      mapOf(
        "crn" to personManagerEntity.crn,
        "teamCode" to personManagerEntity.teamCode,
        "staffId" to personManagerEntity.staffId.toString(10)
      )
    )
  }

  private fun trackEvent(eventType: TelemetryEventType, customDimensions: Map<String, String?>) {
    telemetryClient.trackEvent(eventType.eventName, customDimensions, null)
  }
}
