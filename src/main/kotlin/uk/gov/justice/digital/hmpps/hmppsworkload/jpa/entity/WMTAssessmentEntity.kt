package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "arms", schema = "staging")
data class WMTAssessmentEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column
  val assessmentDate: String? = null,

  @Column
  val assessmentCode: String? = null,

  @Column(name = "assessment_desc")
  val assessmentDescription: String? = null,

  @Column(name = "assessment_staff_name")
  val staffName: String? = null,

  @Column(name = "assessment_staff_key")
  val staffCode: String,

  @Column(name = "assessment_staff_grade")
  val staffGrade: String? = null,

  @Column(name = "assessment_team_key")
  val teamCode: String,

  @Column
  val crn: String? = null,

  @Column
  val disposalOrReleaseDate: String? = null,

  @Column
  val sentenceType: String,

  @Column(name = "so_registration_date")
  val registrationDate: String? = null,

  @Column(name = "asmnt_outcome_cd")
  val outcomeCode: String? = null,

  @Column(name = "asmnt_outcome_desc")
  val outcomeDescription: String? = null,

  @Column(name = "last_saved_dt_referral_doc")
  val lastSavedReferralDocumentDate: String? = null,

  @Column(name = "last_saved_dt_assessment_doc")
  val lastSavedAssessmentDocumentDate: String? = null,

  @Column(name = "offender_manager_staff_name")
  val personManagerName: String? = null,

  @Column(name = "offender_manager_team_cd")
  val personManagerTeamCode: String? = null,

  @Column(name = "offender_manager_cluster_cd")
  val personManagerClusterCode: String? = null,

  @Column(name = "offender_manager_provider_cd")
  val personManagerProviderCode: String? = null,

  @Column
  val completedDate: String? = null,

  @Column(name = "offender_manager_pdu_cd")
  val personManagerPduCode: String? = null,

)
