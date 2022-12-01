package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.math.BigInteger
import java.util.UUID

interface EventManagerRepository : CrudRepository<EventManagerEntity, Long> {
  fun findFirstByCrnAndEventIdOrderByCreatedDateDesc(crn: String, eventId: BigInteger): EventManagerEntity?
  fun findByUuid(id: UUID): EventManagerEntity?

  fun findFirstByStaffCodeAndTeamCodeAndIsActiveTrueOrderByCreatedDateDesc(staffCode: String, teamCode: String): EventManagerEntity?

  @Modifying
  @Query("update EventManagerEntity e set e.isActive= false where e.crn = ?1")
  fun setInactiveTrueFor(crn: String): Int

  fun findFirstByEventIdOrderByCreatedDateDesc(eventId: BigInteger): EventManagerEntity?

  fun findFirstByCrnAndEventNumberOrderByCreatedDateDesc(crn: String, eventNumber: Int): EventManagerEntity?
}
