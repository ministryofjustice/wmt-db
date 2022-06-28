package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

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
  val points: Int
)
