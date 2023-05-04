package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal

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
