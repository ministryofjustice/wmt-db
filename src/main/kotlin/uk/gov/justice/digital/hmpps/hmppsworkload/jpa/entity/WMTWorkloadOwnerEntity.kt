package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "workload_owner", schema = "app")
data class WMTWorkloadOwnerEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @OneToOne
  @JoinColumn(name = "team_id")
  val team: TeamEntity,

  @OneToOne
  @JoinColumn(name = "offender_manager_id")
  val offenderManager: OffenderManagerEntity,

  @Column
  val contractedHours: BigDecimal,

)
