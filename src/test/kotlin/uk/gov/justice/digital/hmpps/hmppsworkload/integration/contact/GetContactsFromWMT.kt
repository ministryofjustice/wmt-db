package uk.gov.justice.digital.hmpps.hmppsworkload.integration.contact

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTCMSEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WMTGetContacts

class GetContactsFromWMT : IntegrationTestBase() {

  @Autowired
  protected lateinit var wmtGetContacts: WMTGetContacts

  @Test
  fun `must return all contacts performed when not the person manager and in a different team`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val contactTypeCode = "CONTACT1"
    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffCode, staffTeamCode = teamCode, personManagerStaffCode = "OTHERSTAFFCODE", personManagerTeamCode = "TM2", contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsOutsideCaseload(staffCode, teamCode)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(contactTypeCode, results[0].typeCode)
  }

  @Test
  fun `must return all contacts performed when not the person manager and in the same team`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val contactTypeCode = "CONTACT1"
    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffCode, staffTeamCode = teamCode, personManagerStaffCode = "OTHERSTAFFCODE", personManagerTeamCode = teamCode, contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsOutsideCaseload(staffCode, teamCode)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(contactTypeCode, results[0].typeCode)
  }

  @Test
  fun `must return empty list when no cms entries exist`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"

    val results = wmtGetContacts.findContactsOutsideCaseload(staffCode, teamCode)

    Assertions.assertTrue(results.isEmpty())
  }

  @Test
  fun `must not return contacts if its the same person performing the contact and is the person manager`() {
    val staffCode = "STAFF1"
    val teamCode = "TM1"
    val contactTypeCode = "CONTACT1"
    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffCode, staffTeamCode = teamCode, personManagerStaffCode = staffCode, personManagerTeamCode = teamCode, contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsOutsideCaseload(staffCode, teamCode)

    Assertions.assertTrue(results.isEmpty())
  }
}
