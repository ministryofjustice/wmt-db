package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.math.BigDecimal
import java.math.BigInteger
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@TypeDefs(
  TypeDef(name = "json", typeClass = JsonStringType::class),
  TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
@Table(name = "workload_calculation")
data class WorkloadCalculationEntity(
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val calculationId: Long? = null,

  @Column
  val calculatedDate: ZonedDateTime = ZonedDateTime.now(),

  @Column
  val weeklyHours: BigDecimal,

  @Column
  val reductions: BigDecimal,

  @Column
  val availablePoints: BigInteger,

  @Column
  val workloadPoints: BigInteger,

  @Column
  val staffCode: String,

  @Column
  val teamCode: String,

  @Column
  val providerCode: String,

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  val breakdownData: BreakdownDataEntity

)
