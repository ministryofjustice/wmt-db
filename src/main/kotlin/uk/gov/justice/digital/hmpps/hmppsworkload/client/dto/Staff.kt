package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class Staff @JsonCreator constructor(
  val staffIdentifier: Long,
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
