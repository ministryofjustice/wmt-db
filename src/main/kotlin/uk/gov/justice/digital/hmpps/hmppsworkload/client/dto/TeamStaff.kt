package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.deliusToStaffGrade

data class TeamStaff @JsonCreator constructor(
  val staffCode: String,
  val staff: StaffName,
  val staffGrade: StaffGrade?,
  val email: String?
) {
  var grade: String = deliusToStaffGrade(staffGrade?.code)
}
