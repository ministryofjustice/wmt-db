package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus

@Repository
interface ReductionsRepository : CrudRepository<ReductionEntity, Long> {
  fun findByStatusIsInAndWorkloadOwnerIdIs(statuses: List<ReductionStatus>, workloadOwnerId: Long): List<ReductionEntity>
}
