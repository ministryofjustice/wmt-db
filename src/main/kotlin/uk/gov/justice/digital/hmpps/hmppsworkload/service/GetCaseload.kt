package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case

interface GetCaseload {
  fun getCases(staffCode: String, teamCode: String): List<Case>

  fun getLastAllocatedCase(staffCode: String, teamCode: String): Case?
}
