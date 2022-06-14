package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsId

interface CaseDetailsRepository : CrudRepository<CaseDetailsEntity, CaseDetailsId> {

  fun findByCrn(crn: String): CaseDetailsEntity?
}
