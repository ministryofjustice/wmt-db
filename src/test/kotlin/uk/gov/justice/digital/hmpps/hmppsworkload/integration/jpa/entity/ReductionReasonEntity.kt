package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

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
