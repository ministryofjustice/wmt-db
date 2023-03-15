package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import java.time.ZonedDateTime

@Repository
interface ReductionsRepository : CrudRepository<ReductionEntity, Long> {

  @Query("select r from ReductionEntity r where r.workloadOwner = ?1 and r.status not in ?2 and (r.effectiveFrom > ?3 or r.effectiveTo > ?4)")
  fun findUpcomingReductions(
    workloadOwner: WMTWorkloadOwnerEntity,
    statuses: List<ReductionStatus>,
    effectiveFrom: ZonedDateTime,
    effectiveTo: ZonedDateTime,
  ): List<ReductionEntity>

  fun findByWorkloadOwnerAndEffectiveFromLessThanAndEffectiveToGreaterThanAndStatusNotIn(workloadOwner: WMTWorkloadOwnerEntity, effectiveFrom: ZonedDateTime, effectiveTo: ZonedDateTime, statuses: List<ReductionStatus>): List<ReductionEntity>

  fun findByEffectiveToBeforeAndStatus(effectiveTo: ZonedDateTime, status: ReductionStatus): List<ReductionEntity>

  fun findByEffectiveFromBeforeAndEffectiveToAfterAndStatus(effectiveFrom: ZonedDateTime, effectiveTo: ZonedDateTime, status: ReductionStatus): List<ReductionEntity>
}
