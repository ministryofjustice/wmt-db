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
  @JoinColumn(name = "tier_code")
  val tierCategory: CaseCategoryEntity,

  @Column
  val gradeCode: String = "PSP",

  @Column(name = "location")
  @Enumerated(EnumType.STRING)
  val caseType: CaseType,

)
