package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Component

@Component
class DefaultGradeMapper : GradeMapper {
  private val gradeMap: Map<String, String> = mapOf("PSQ" to "PSO", "PSP" to "PQiP", "PSM" to "PO", "PSC" to "SPO", "TPO" to "PQiP", "NPSM" to "PO")

  override fun deliusToStaffGrade(deliusCode: String?): String {
    return gradeMap[deliusCode] ?: "DMY"
  }

  override fun workloadToStaffGrade(workloadGrade: String): String {
    return gradeMap.getOrDefault(workloadGrade, workloadGrade)
  }
}
