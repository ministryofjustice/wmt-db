package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment

interface GetAssessments {

  fun getAssessments(staffCode: String, teamCode: String): List<Assessment>
}
