package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SCORE_UNAVAILABLE
import java.time.LocalDate

data class OffenderAssessment @JsonCreator constructor(
  val ogrsLastUpdate: LocalDate?,
  val ogrsScore: Int?
) {
  fun getOgrsLevel(): String = ogrsScore?.let {
    when {
      it <= 49 -> "Low"
      it in 50..74 -> "Medium"
      it in 75..89 -> "High"
      else -> "Very High"
    }
  } ?: SCORE_UNAVAILABLE
}
