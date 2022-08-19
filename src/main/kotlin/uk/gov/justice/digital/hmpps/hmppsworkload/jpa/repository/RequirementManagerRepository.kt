package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.math.BigInteger
import java.util.UUID

interface RequirementManagerRepository : CrudRepository<RequirementManagerEntity, Long> {
  fun findFirstByCrnAndEventIdAndRequirementIdOrderByCreatedDateDesc(crn: String, eventId: BigInteger, requirementId: BigInteger): RequirementManagerEntity?
  fun findByUuid(id: UUID): RequirementManagerEntity?

  @Modifying
  @Query("update RequirementManagerEntity r set r.isActive= false where r.crn = ?1")
  fun setInactiveTrueFor(crn: String): Int
}
