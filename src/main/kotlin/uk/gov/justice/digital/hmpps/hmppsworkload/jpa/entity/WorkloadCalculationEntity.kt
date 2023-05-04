package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Type
import java.math.BigInteger
import java.time.ZonedDateTime

@Entity
@Table(name = "workload_calculation")
data class WorkloadCalculationEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val calculationId: Long? = null,

  @Column
  val calculatedDate: ZonedDateTime = ZonedDateTime.now(),

  @Column
  val availablePoints: BigInteger,

  @Column
  val workloadPoints: BigInteger,

  @Column
  val staffCode: String,

  @Column
  val teamCode: String,

  @Type(JsonType::class)
  @Column(columnDefinition = "jsonb")
  val breakdownData: BreakdownDataEntity,

)
