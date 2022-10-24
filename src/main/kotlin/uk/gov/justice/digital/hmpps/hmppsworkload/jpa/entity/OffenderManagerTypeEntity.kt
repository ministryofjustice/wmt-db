package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "offender_manager_type", schema = "app")
data class OffenderManagerTypeEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  val gradeCode: String
)
