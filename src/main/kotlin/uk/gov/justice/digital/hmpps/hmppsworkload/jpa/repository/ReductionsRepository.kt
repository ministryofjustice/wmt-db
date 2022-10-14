package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import java.time.ZonedDateTime

@Repository
interface ReductionsRepository : CrudRepository<ReductionEntity, Long> {

  fun findByWorkloadOwnerAndEffectiveFromGreaterThanOrEffectiveToGreaterThanAndStatusNotIn(workloadOwner: WMTWorkloadOwnerEntity, effectiveFrom: ZonedDateTime, effectiveTo: ZonedDateTime, statuses: List<ReductionStatus>): List<ReductionEntity>

  fun findByWorkloadOwnerAndEffectiveFromLessThanAndEffectiveToGreaterThanAndStatusNotIn(workloadOwner: WMTWorkloadOwnerEntity, effectiveFrom: ZonedDateTime, effectiveTo: ZonedDateTime, statuses: List<ReductionStatus>): List<ReductionEntity>

  fun findByEffectiveToBeforeAndStatus(effectiveTo: ZonedDateTime, status: ReductionStatus): List<ReductionEntity>

  fun findByEffectiveFromBeforeAndEffectiveToAfterAndStatus(effectiveFrom: ZonedDateTime, effectiveTo: ZonedDateTime, status: ReductionStatus): List<ReductionEntity>
}
