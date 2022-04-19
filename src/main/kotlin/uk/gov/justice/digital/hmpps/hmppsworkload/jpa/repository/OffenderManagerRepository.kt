package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.OffenderManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerCaseloadTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview

@Repository
interface OffenderManagerRepository : CrudRepository<OffenderManagerEntity, Long> {
  @Query(nativeQuery = true)
  fun findByOverview(teamCode: String, offenderManagerCode: String): OffenderManagerOverview?
  @Query(nativeQuery = true)
  fun findByCaseloadTotals(workloadOwnerId: Long): List<OffenderManagerCaseloadTotals>
  @Query(nativeQuery = true)
  fun findCasesByTeamCodeAndStaffCode(teamCode: String, offenderManagerCode: String): List<OffenderManagerCase>
  @Query(nativeQuery = true)
  fun findCaseByTeamCodeAndStaffCodeAndCrn(teamCode: String, offenderManagerCode: String, crn: String): OffenderManagerCase?
}
