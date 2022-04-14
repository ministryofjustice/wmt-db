package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.util.UUID

interface GetRequirementManager {
  fun findById(id: UUID): RequirementManagerEntity?
}
