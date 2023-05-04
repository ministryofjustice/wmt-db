package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cms", schema = "staging")
data class WMTCMSEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column
  val contactTypeCode: String,

  @Column(name = "contact_staff_key")
  val staffCode: String,

  @Column(name = "contact_team_key")
  val staffTeamCode: String,

  @Column(name = "om_key")
  val personManagerStaffCode: String,

  @Column(name = "om_team_key")
  val personManagerTeamCode: String,
)
