package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.OffenderManagerTypeEntity

interface OffenderManagerTypeRepository : CrudRepository<OffenderManagerTypeEntity, Long> {
  fun findByGradeCode(gradeCode: String): OffenderManagerTypeEntity?
}
