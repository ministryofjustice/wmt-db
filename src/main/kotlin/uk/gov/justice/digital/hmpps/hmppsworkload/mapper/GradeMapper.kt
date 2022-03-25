package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

interface GradeMapper {

  fun deliusToStaffGrade(deliusCode: String?): String
  fun workloadToStaffGrade(workloadGrade: String): String
}
