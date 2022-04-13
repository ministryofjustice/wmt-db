package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class Staff @JsonCreator constructor(
  val staffIdentifier: BigInteger,
  val staffCode: String,
  val staff: StaffName,
  val staffGrade: StaffGrade?,
  val teams: List<Team>?,
  val probationArea: StaffProbationArea?,
  val email: String?
)

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
