package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigInteger
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "EVENT_MANAGER")
@EntityListeners(AuditingEntityListener::class)
data class EventManagerEntity(
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
  val eventId: BigInteger,

  @Column
  val staffCode: String,

  @Column
  val teamId: BigInteger? = null,

  @Column
  val teamCode: String,

  @Column
  var createdBy: String? = null,

  @Column
  @CreatedDate
  var createdDate: ZonedDateTime? = null
)
