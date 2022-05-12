package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerDetails
import java.util.UUID

interface GetPersonManager {
  fun findById(id: UUID): PersonManagerDetails?
}
