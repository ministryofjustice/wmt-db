package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import java.util.UUID

@Service
class JpaBasedGetEventManager(private val eventManagerRepository: EventManagerRepository) : GetEventManager {
  override fun findById(id: UUID): EventManagerEntity? = eventManagerRepository.findByUuid(id)
}
