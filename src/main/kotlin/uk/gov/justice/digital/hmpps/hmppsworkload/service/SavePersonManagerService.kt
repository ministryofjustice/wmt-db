package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import java.math.BigInteger

interface SavePersonManagerService {

  fun savePersonManager(teamCode: String, staffId: BigInteger, allocateCase: AllocateCase, loggedInUser: String): PersonManagerEntity
}
