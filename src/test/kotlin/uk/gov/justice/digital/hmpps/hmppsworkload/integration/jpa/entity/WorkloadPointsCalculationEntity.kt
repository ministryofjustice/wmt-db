package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "workload_points_calculations", schema = "app")
data class WorkloadPointsCalculationEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @ManyToOne
  @JoinColumn(name = "workload_report_id")
  val workloadReport: WorkloadReportEntity,

  @ManyToOne
  @JoinColumn(name = "workload_points_id")
  val workloadPoints: WorkloadPointsEntity,

  @ManyToOne
  @JoinColumn(name = "t2a_workload_points_id")
  val t2aWorkloadPoints: WorkloadPointsEntity,

  @OneToOne
  @JoinColumn(name = "workload_id")
  val workload: WMTWorkloadEntity,

  @Column
  val totalPoints: Int = 0,

  @Column(name = "sdr_points")
  val standardDeliveryReportPoints: Int = 0,

  @Column(name = "sdr_conversion_points")
  val fastDeliveryReportPoints: Int = 0,

  @Column(name = "paroms_points")
  val institutionalReportPoints: Int = 0,

  @Column(name = "nominal_target")
  val defaultAvailablePointsForGrade: Int = 0,

  @Column
  val availablePoints: Int = 0,

  @Column
  val reductionHours: BigDecimal = BigDecimal.ZERO,

  @Column
  val contractedHours: BigDecimal = BigDecimal.ZERO,

  @Column
  val cmsAdjustmentPoints: Int = 0,

  @Column(name = "arms_total_cases")
  val assessmentPoints: Int = 0,

  @Column
  val lastUpdatedOn: LocalDateTime = LocalDateTime.now().with(LocalTime.of(9, 30)),
)
