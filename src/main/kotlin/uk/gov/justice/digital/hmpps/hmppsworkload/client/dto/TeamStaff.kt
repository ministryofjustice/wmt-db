package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.deliusToStaffGrade
import java.math.BigInteger

data class TeamStaff @JsonCreator constructor(
  val staffIdentifier: BigInteger,
  val staffCode: String,
  val staff: StaffName,
  val staffGrade: StaffGrade?,
) {
  var grade: String = deliusToStaffGrade(staffGrade?.code)
}
