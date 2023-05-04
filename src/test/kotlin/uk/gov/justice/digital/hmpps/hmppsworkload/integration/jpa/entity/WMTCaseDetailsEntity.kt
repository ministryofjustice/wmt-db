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
@Table(name = "case_details", schema = "app")
data class WMTCaseDetailsEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @OneToOne
  @JoinColumn(name = "workload_id")
  val workload: WMTWorkloadEntity,

  @Column
  val rowType: String = "N",

  @Column(name = "case_ref_no")
  val crn: String,

  @OneToOne
  @JoinColumn(name = "tier_code", referencedColumnName = "categoryId")
  val tierCategory: CaseCategoryEntity,

  @Column
  val gradeCode: String = "PSP",

  @Column(name = "location")
  @Enumerated(EnumType.STRING)
  val caseType: CaseType,

  @Column
  val teamCode: String,
)
