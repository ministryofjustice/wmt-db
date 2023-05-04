package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Entity
@Table(name = "tiers", schema = "app")
data class TiersEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @OneToOne
  @JoinColumn(name = "workload_id")
  val workload: WMTWorkloadEntity,

  @Column(name = "location")
  @Enumerated(EnumType.STRING)
  val caseType: CaseType,

  @OneToOne
  @JoinColumn(name = "tier_number", referencedColumnName = "categoryId")
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
