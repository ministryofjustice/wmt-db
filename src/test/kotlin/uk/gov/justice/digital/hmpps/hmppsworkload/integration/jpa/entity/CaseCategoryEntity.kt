package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

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
