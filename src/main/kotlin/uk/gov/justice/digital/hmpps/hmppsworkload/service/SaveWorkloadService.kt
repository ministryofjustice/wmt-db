package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated

interface SaveWorkloadService {

  fun saveWorkload(teamCode: String, staffCode: String, allocateCase: AllocateCase, loggedInUser: String, authToken: String): CaseAllocated
}
