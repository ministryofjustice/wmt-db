package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "reductions", schema = "app")
data class ReductionEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @ManyToOne
  @JoinColumn(name = "workload_owner_id")
  val workloadOwner: WMTWorkloadOwnerEntity,

  @Column(name = "hours")
  val hours: BigDecimal,

  @Column(name = "effective_from")
  val effectiveFrom: ZonedDateTime,

  @Column(name = "effective_to")
  val effectiveTo: ZonedDateTime,

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  var status: ReductionStatus,

  @Column(name = "reduction_reason_id")
  val reductionReasonId: Long
)
