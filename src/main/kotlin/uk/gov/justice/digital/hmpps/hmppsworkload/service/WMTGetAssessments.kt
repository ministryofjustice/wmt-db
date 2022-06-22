package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTAssessmentRepository
import java.util.Locale

@Service
class WMTGetAssessments(private val wmtAssessmentRepository: WMTAssessmentRepository) : GetAssessments {
  override fun getAssessments(staffCode: String, teamCode: String): List<Assessment> =
    wmtAssessmentRepository.findByTeamCodeAndStaffCode(teamCode, staffCode).let { wmtAssessments ->
      wmtAssessments.map { wmtAssessment -> Assessment(sentenceTypeToCaseType(wmtAssessment.sentenceType)) }
    }

  private fun sentenceTypeToCaseType(sentenceType: String): CaseType {
    return try {
      CaseType.valueOf(sentenceType.uppercase(Locale.getDefault()))
    } catch (e: IllegalArgumentException) {
      log.error("Unknown assessment mapped", e)
      CaseType.UNKNOWN
    }
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
