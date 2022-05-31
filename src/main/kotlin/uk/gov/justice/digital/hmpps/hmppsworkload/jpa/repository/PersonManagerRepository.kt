package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import java.time.ZonedDateTime
import java.util.UUID

interface PersonManagerRepository : CrudRepository<PersonManagerEntity, Long> {
  fun findFirstByCrnOrderByCreatedDateDesc(crn: String): PersonManagerEntity?
  fun findByUuid(id: UUID): PersonManagerEntity?
  @Query(nativeQuery = true)
  fun findByTeamCodeAndCreatedDateGreaterThanEqualLatest(teamCode: String, createdDate: ZonedDateTime): List<PersonManagerEntity>

}
