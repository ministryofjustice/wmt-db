package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigInteger
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EntityResult
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.NamedNativeQuery
import javax.persistence.SqlResultSetMapping
import javax.persistence.Table

@SqlResultSetMapping(
  name = "PersonManagerEntity",
  entities = [
    EntityResult(entityClass = PersonManagerEntity::class)
  ]
)
@NamedNativeQuery(
  name = "PersonManagerEntity.findByTeamCodeAndCreatedDateGreaterThanEqualLatest",
  resultSetMapping = "PersonManagerEntity",
  query = """
SELECT DISTINCT ON (crn) *
FROM person_manager pm
WHERE pm.team_code = ?1 AND pm.created_date >= ?2
ORDER BY crn, created_date DESC;
"""
)

@Entity
@Table(name = "PERSON_MANAGER")
@EntityListeners(AuditingEntityListener::class)
data class PersonManagerEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column
  val uuid: UUID = UUID.randomUUID(),

  @Column
  val crn: String,

  @Column
  val staffId: BigInteger,

  @Column
  val staffCode: String,

  @Column
  val teamId: BigInteger? = null,

  @Column
  val teamCode: String,

  @Column
  val offenderName: String,

  @Column
  var createdBy: String,

  @Column
  @CreatedDate
  var createdDate: ZonedDateTime? = null,

  @Column
  var providerCode: String,

  @Column
  var isActive: Boolean

)
