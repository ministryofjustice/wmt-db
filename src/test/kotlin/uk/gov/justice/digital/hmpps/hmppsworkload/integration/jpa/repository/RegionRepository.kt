package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RegionEntity

interface RegionRepository : CrudRepository<RegionEntity, Long> {
  fun findByCode(code: String): RegionEntity?
}
