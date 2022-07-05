package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTAssessmentEntity

@Repository
interface WMTAssessmentRepository : CrudRepository<WMTAssessmentEntity, Long> {
  fun findByStaffCodeAndTeamCode(staffCode: String, teamCode: String): List<WMTAssessmentEntity>
}
