package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import java.util.UUID

@Service
class JpaBasedGetRequirementManager(private val requirementManagerRepository: RequirementManagerRepository) : GetRequirementManager {
  override fun findById(id: UUID): RequirementManagerEntity? = requirementManagerRepository.findByUuid(id)
}
