package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.util.UUID

interface GetEventManager {
  fun findById(id: UUID): EventManagerEntity?
}
