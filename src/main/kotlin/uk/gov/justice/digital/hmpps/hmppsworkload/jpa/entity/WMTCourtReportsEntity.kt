package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "court_reports", schema = "staging")
data class WMTCourtReportsEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "team_desc")
  val teamName: String? = null,

  @Column
  val teamCode: String,

  @Column(name = "om_key")
  val staffCode: String,

  @Column(name = "om_team_staff_grade")
  val teamStaffGradeCode: String? = null,

  @Column(name = "sdr_last_30")
  val standardDeliveryReportCount: Int? = null,

  @Column(name = "sdr_due_next_30")
  val standardDeliveryReportsDueCount: Int? = null,

  @Column(name = "sdr_conv_last_30")
  val fastDeliveryReportCount: Int? = null,

  @Column(name = "datestamp")
  val effectiveFrom: String? = null,

  @Column
  val oralReports: String? = null,

  @Column
  val trust: String? = null,

  @Column(name = "region_desc")
  val regionName: String? = null,

  @Column
  val regionCode: String? = null,

  @Column(name = "ldu_desc")
  val lduName: String? = null,

  @Column
  val lduCode: String? = null,

  @Column(name = "om_surname")
  val staffSurname: String? = null,

  @Column(name = "om_forename")
  val staffForename: String? = null,

  @Column(name = "om_grade_code")
  val staffGradeCode: String? = null,

  @Column
  val pduCode: String? = null,

  @Column(name = "pdu_desc")
  val pduName: String? = null

)
