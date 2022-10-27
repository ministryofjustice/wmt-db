package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionReasonEntity

interface ReductionReasonRepository : CrudRepository<ReductionReasonEntity, Long>
