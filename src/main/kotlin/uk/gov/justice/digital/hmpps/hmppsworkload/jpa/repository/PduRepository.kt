package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PduEntity

interface PduRepository : CrudRepository<PduEntity, Long> {
  fun findByCode(code: String): PduEntity?
}
