package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "adjustment_reason", schema = "app")
data class AdjustmentReasonEntity constructor(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "contact_code")
  val typeCode: String,

  @Column(name = "category_id")
  val categoryId: Long = 1,

  @Column(name = "points")
  val points: Int,
)
