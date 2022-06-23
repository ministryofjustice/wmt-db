package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTCMSEntity

@Repository
interface WMTCMSRepository : CrudRepository<WMTCMSEntity, Long> {
  fun findByStaffTeamCodeAndStaffCodeAndPersonManagerStaffCodeNot(staffTeamCode: String, staffCode: String, personManagerStaffCode: String): List<WMTCMSEntity>

  fun findByPersonManagerTeamCodeAndPersonManagerStaffCodeAndStaffCodeNot(personManagerTeamCode: String, personManagerStaffCode: String, staffCode: String): List<WMTCMSEntity>
}
