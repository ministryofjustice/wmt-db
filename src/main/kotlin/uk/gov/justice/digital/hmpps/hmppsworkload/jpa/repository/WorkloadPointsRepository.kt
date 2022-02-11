package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity

@Repository
interface WorkloadPointsRepository : CrudRepository<WorkloadPointsEntity, Long> {

  fun findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(isT2A: Boolean): WorkloadPointsEntity
}
