package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case

interface GetCaseLoad {
  fun getCases(staffCode: String, teamCode: String): List<Case>
}
