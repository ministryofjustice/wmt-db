package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class StaffSummary @JsonCreator constructor(
  val staffIdentifier: BigInteger,
  val staffCode: String,
  val staff: StaffName,
  val staffGrade: StaffGrade?,
) {
  lateinit var grade: String
}
