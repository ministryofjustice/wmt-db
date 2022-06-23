package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "inst_reports", schema = "staging")
data class WMTInstitutionalReportEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "om_key")
  val staffCode: String,

  @Column(name = "team_code")
  val teamCode: String,

  @Column(name = "parom_comp_last_30")
  val paroleReports: Int?,
)
