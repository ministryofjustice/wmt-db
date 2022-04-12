package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.math.BigInteger

interface EventManagerRepository : CrudRepository<EventManagerEntity, Long> {
  fun findFirstByCrnAndEventIdOrderByCreatedDateDesc(crn: String, eventId: BigInteger): EventManagerEntity?
}
