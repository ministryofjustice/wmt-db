package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseAllocated
import java.math.BigInteger

@Service
class DefaultSaveWorkloadService(private val savePersonManagerService: SavePersonManagerService) : SaveWorkloadService {
  override fun saveWorkload(
    teamCode: String,
    staffId: BigInteger,
    allocateCase: AllocateCase,
    loggedInUser: String
  ): CaseAllocated {
    val personManagerId = savePersonManagerService.savePersonManager(teamCode, staffId, allocateCase, loggedInUser).uuid
    return CaseAllocated(personManagerId)
  }
}
