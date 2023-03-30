package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerAuditEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity

interface EventManagerAuditRepository : CrudRepository<EventManagerAuditEntity, Long> {
  fun findByEventManager(eventManager: EventManagerEntity): List<EventManagerAuditEntity>
}
