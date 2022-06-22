package uk.gov.justice.digital.hmpps.hmppsworkload.integration.assessments

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTAssessmentEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WMTGetAssessments

class GetAssessmentsFromWMT : IntegrationTestBase() {

  @Autowired
  protected lateinit var wmtGetAssessments: WMTGetAssessments

  @Test
  fun `must return community assessments`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    wmtAssessmentRepository.save(WMTAssessmentEntity(staffCode = staffCode, teamCode = teamCode, sentenceType = "Community"))

    val results = wmtGetAssessments.getAssessments(staffCode, teamCode)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(CaseType.COMMUNITY, results[0].category)
  }

  @Test
  fun `must return license assessments`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    wmtAssessmentRepository.save(WMTAssessmentEntity(staffCode = staffCode, teamCode = teamCode, sentenceType = "License"))

    val results = wmtGetAssessments.getAssessments(staffCode, teamCode)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(CaseType.LICENSE, results[0].category)
  }

  @Test
  fun `must return empty list when no assessments are returned`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val results = wmtGetAssessments.getAssessments(staffCode, teamCode)
    Assertions.assertTrue(results.isEmpty())
  }
}
