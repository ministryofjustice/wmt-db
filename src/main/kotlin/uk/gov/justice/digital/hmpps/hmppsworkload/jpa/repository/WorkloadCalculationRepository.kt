package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity

@Repository
interface WorkloadCalculationRepository : CrudRepository<WorkloadCalculationEntity, Long> {
  fun findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(
    staffCode: String,
    teamCode: String,
  ): WorkloadCalculationEntity?
}
