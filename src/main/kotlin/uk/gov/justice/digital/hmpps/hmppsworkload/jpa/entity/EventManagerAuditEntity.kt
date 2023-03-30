package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "EVENT_MANAGER_AUDIT")
@EntityListeners(AuditingEntityListener::class)
data class EventManagerAuditEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column
  val allocationJustificationNotes: String,

  @Column
  val sensitiveNotes: Boolean,

  @Column
  val createdBy: String,

  @Column
  @CreatedDate
  var createdDate: ZonedDateTime? = null,

  @ManyToOne
  @JoinColumn(name = "event_manager_id")
  val eventManager: EventManagerEntity,
)
