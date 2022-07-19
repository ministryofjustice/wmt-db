package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class Staff @JsonCreator constructor(
  val staffIdentifier: BigInteger,
  val staffCode: String,
  val staff: StaffName,
  val staffGrade: StaffGrade? = null,
  val teams: List<Team>? = null,
  val probationArea: StaffProbationArea? = null,
  val email: String? = null
) {
  lateinit var grade: String
}

data class StaffName @JsonCreator constructor(
  val forenames: String,
  val surname: String
)

data class StaffGrade @JsonCreator constructor(
  val code: String
)

data class Team @JsonCreator constructor(
  val code: String,
  val description: String
)

data class StaffProbationArea @JsonCreator constructor(
  val code: String
)
