package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated
import java.math.BigInteger

interface SaveWorkloadService {

  fun saveWorkload(teamCode: String, staffId: BigInteger, allocateCase: AllocateCase, loggedInUser: String): CaseAllocated
}
