package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tiers", schema = "app")
data class TiersEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @OneToOne
  @JoinColumn(name = "workload_id")
  val workload: WorkloadEntity,

  @Column(name = "location")
  @Enumerated(EnumType.STRING)
  val caseType: CaseType,

  @OneToOne
  @JoinColumn(name = "tier_number")
  val tierCategory: CaseCategoryEntity,

  @Column
  val overdueTerminationsTotal: Int = 0,

  @Column
  val warrantsTotal: Int = 0,

  @Column
  val unpaidWorkTotal: Int = 0,

  @Column
  val totalCases: Int = 0,

  @Column
  val t2aOverdueTerminationsTotal: Int = 0,

  @Column
  val t2aWarrantsTotal: Int = 0,

  @Column
  val t2aUnpaidWorkTotal: Int = 0,

  @Column
  val t2aTotalCases: Int = 0,

  @Column
  val suspendedTotal: Int = 0,

  @Column
  val suspendedLiferTotal: Int = 0,

  @Column
  val totalFilteredCases: Int = 0,

)
