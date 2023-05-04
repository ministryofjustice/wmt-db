package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "case_category", schema = "app")
data class CaseCategoryEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
  @Column
  val categoryId: Int = 1,
  @Column
  val categoryName: String = "A1",
) : Serializable
