package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "workload_report", schema = "app")
data class WorkloadReportEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "effective_from")
  val effectiveFrom: ZonedDateTime = ZonedDateTime.now(),

  @Column(name = "effective_to")
  val effectiveTo: ZonedDateTime? = null,

  @Column
  val status: String = "COMPLETE",

  @Column
  val statusDescription: String = "COMPLETE",
)
