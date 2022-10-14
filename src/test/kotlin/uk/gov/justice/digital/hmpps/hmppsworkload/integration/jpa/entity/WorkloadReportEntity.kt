package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

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
