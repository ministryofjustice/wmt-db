package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class StaffActiveCases @JsonCreator constructor(
  val code: String,
  val name: Name,
  private val grade: String?,
  val email: String?,
  val cases: List<ActiveCase>,
) {
  fun getGrade(): String = grade ?: "DMY"
}

data class ActiveCase(
  val crn: String,
  val name: Name,
  val type: String
)
