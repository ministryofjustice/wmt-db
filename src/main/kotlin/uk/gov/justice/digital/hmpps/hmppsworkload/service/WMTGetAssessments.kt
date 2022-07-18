package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTAssessmentRepository
import java.util.Locale

@Service
class WMTGetAssessments(private val wmtAssessmentRepository: WMTAssessmentRepository) : GetAssessments {
  override fun getAssessments(staffCode: String, teamCode: String): List<Assessment> =
    wmtAssessmentRepository.findByStaffCodeAndTeamCode(staffCode, teamCode).let { wmtAssessments ->
      wmtAssessments.map { Assessment(getCaseType(it.sentenceType)) }
    }

  private fun getCaseType(wmtType: String): CaseType {
    val type = wmtType.uppercase(Locale.getDefault())
    if (type == "LICENCE") {
      return CaseType.LICENSE
    }
    return CaseType.valueOf(type)
  }
}
