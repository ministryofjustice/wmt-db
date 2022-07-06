package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTCourtReportsEntity

@Repository
interface WMTCourtReportsRepository : CrudRepository<WMTCourtReportsEntity, Long> {
  fun findByStaffCodeAndTeamCode(staffCode: String, teamCode: String): WMTCourtReportsEntity?
}
