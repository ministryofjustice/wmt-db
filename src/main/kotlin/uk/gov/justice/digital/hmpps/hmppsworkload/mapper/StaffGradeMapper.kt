package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

private val gradeMap: Map<String, String> = mapOf("PSQ" to "PSO", "PSP" to "PQiP", "PSM" to "PO", "PSC" to "SPO", "TPO" to "PQiP", "NPSM" to "PO")

fun deliusToStaffGrade(deliusCode: String?): String {
  return gradeMap[deliusCode] ?: "DMY"
}
