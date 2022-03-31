package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class Staff @JsonCreator constructor(
  val staffIdentifier: BigInteger,
  val staffCode: String,
  val staff: StaffName,
  val staffGrade: StaffGrade?
)

data class StaffName @JsonCreator constructor(
  val forenames: String,
  val surname: String
)

data class StaffGrade @JsonCreator constructor(
  val code: String
)
