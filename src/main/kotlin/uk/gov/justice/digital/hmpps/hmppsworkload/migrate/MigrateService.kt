package uk.gov.justice.digital.hmpps.hmppsworkload.migrate

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository

@Service
class MigrateService(
  private val eventManagerRepository: EventManagerRepository,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val requirementManagerRepository: RequirementManagerRepository
) {
  fun migrateEventIdentifiers() {
    eventManagerRepository.findByEventNumberIsNull().forEach { eventManagerEntity ->
      val conviction = communityApiClient.getConvictionById(eventManagerEntity.crn, eventManagerEntity.eventId)
        .block()
      conviction?.let {
        eventManagerEntity.eventNumber = it.eventNumber
        eventManagerRepository.save(eventManagerEntity)
      } ?: log.info("unable to retrieve event manager ${eventManagerEntity.crn} with event ID ${eventManagerEntity.eventId}")
    }

    requirementManagerRepository.findByEventNumberIsNull().forEach { requirementManagerEntity ->
      val conviction = communityApiClient.getConvictionById(requirementManagerEntity.crn, requirementManagerEntity.eventId).block()
      conviction?.let {
        requirementManagerEntity.eventNumber = it.eventNumber
        requirementManagerRepository.save(requirementManagerEntity)
      } ?: log.info("unable to retrieve event manager ${requirementManagerEntity.crn} with event ID ${requirementManagerEntity.eventId}")
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
