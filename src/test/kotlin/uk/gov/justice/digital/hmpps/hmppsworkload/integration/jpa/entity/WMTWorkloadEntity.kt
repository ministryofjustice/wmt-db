package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "workload", schema = "app")
data class WMTWorkloadEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @ManyToOne
  @JoinColumn(name = "workload_owner_id")
  val workloadOwner: WMTWorkloadOwnerEntity,

  @Column
  val totalCases: Int = 0,

  @Column
  val totalCommunityCases: Int = 0,

  @Column
  val totalCustodyCases: Int = 0,

  @Column
  val totalLicenseCases: Int = 0,

  @Column(name = "monthly_sdrs")
  val monthlyStandardDeliveryReports: Int = 0,

  @Column(name = "sdr_due_next_30_days")
  val monthlyStandardDeliveryReportsDueInNextThirtyDays: Int = 0,

  @Column(name = "sdr_conversions_last_30_days")
  val fastDeliveryReportsInLastThirtyDays: Int = 0,

  @Column(name = "paroms_completed_last_30_days")
  val institutionalReportsCompletedInLastThirtyDays: Int = 0,

  @Column(name = "paroms_due_next_30_days")
  val institutionalReportsDueInNextThirtyDays: Int = 0,

  @Column(name = "license_last_16_weeks")
  val licenseCasesInLastSixteenWeeks: Int = 0,

  @Column(name = "community_last_16_weeks")
  val communityCasesInLastSixteenWeeks: Int = 0,

  @Column(name = "arms_community_cases")
  val communityAssessmentCases: Int = 0,

  @Column(name = "arms_license_cases")
  val licenseAssessmentCases: Int = 0,

  @ManyToOne
  @JoinColumn(name = "workload_report_id")
  val workloadReport: WorkloadReportEntity,

  @Column
  val totalFilteredCases: Int = 0,

  @Column
  val totalFilteredCommunityCases: Int = 0,

  @Column
  val totalFilteredCustodyCases: Int = 0,

  @Column
  val totalFilteredLicenseCases: Int = 0,

)
