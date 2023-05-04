package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.ZonedDateTime

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
