package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigDecimal
import java.time.LocalDateTime

data class RiskPredictor @JsonCreator constructor(
  val rsrPercentageScore: BigDecimal?,
  val rsrScoreLevel: String?,
  val completedDate: LocalDateTime?,
)
