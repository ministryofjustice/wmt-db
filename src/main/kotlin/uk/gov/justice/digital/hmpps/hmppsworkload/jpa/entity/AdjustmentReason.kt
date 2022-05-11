package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import java.math.BigInteger
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "workload_points", schema = "app")
data class AdjustmentReason constructor(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,

  @Column(name = "contact_code")
  val typeCode: String,

  @Column(name = "points")
  val points: BigInteger
)
