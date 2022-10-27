package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "reduction_reason", schema = "app")
data class ReductionReasonEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column
  val reason: String = "",

  @Column
  val reasonShortName: String = "",

  @ManyToOne
  @JoinColumn(name = "category_id")
  val reductionCategoryEntity: ReductionCategoryEntity,

)
