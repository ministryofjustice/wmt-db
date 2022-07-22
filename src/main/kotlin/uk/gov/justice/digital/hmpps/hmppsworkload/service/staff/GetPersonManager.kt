package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import java.util.UUID

interface GetPersonManager {
  fun findById(id: UUID): PersonManagerDetails?

  fun findLatestByCrn(crn: String): PersonManager?
}
