package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import java.util.UUID

interface GetPersonManager {
  fun findById(id: UUID): PersonManagerEntity?
}
