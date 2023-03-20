package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class RiskSummary @JsonCreator constructor(
  val overallRiskLevel: String?,
)
