package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTInstitutionalReportEntity

@Repository
interface WMTInstitutionalReportRepository : CrudRepository<WMTInstitutionalReportEntity, Long> {
  fun findByTeamCodeAndStaffCode(teamCode: String, staffCode: String): List<WMTInstitutionalReportEntity>
}
