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

  @Column(name = "assessment_staff_key")
  val staffCode: String,

  @Column(name = "assessment_team_key")
  val teamCode: String,

  @Column
  val sentenceType: String,
)
