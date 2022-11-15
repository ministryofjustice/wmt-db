package uk.gov.justice.digital.hmpps.hmppsworkload.integration.contact

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTCMSEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.WMTGetContacts

class GetContactsFromWMT : IntegrationTestBase() {

  @Autowired
  protected lateinit var wmtGetContacts: WMTGetContacts

  var staffIdentifier = StaffIdentifier("STAFF1", "TM1")
  private val contactTypeCode = "CONTACT1"

  @Test
  fun `must return all contacts performed when not the person manager and in a different team`() {

    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffIdentifier.staffCode, staffTeamCode = staffIdentifier.teamCode, personManagerStaffCode = "OTHERSTAFFCODE", personManagerTeamCode = "TM2", contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsOutsideCaseload(staffIdentifier)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(contactTypeCode, results[0].typeCode)
  }

  @Test
  fun `must return all contacts performed when not the person manager and in the same team`() {

    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffIdentifier.staffCode, staffTeamCode = staffIdentifier.teamCode, personManagerStaffCode = "OTHERSTAFFCODE", personManagerTeamCode = staffIdentifier.teamCode, contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsOutsideCaseload(staffIdentifier)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(contactTypeCode, results[0].typeCode)
  }

  @Test
  fun `must return empty list when no cms entries exist`() {

    val results = wmtGetContacts.findContactsOutsideCaseload(staffIdentifier)

    Assertions.assertTrue(results.isEmpty())
  }

  @Test
  fun `must not return contacts if its the same person performing the contact and is the person manager`() {

    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffIdentifier.staffCode, staffTeamCode = staffIdentifier.teamCode, personManagerStaffCode = staffIdentifier.staffCode, personManagerTeamCode = staffIdentifier.teamCode, contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsOutsideCaseload(staffIdentifier)

    Assertions.assertTrue(results.isEmpty())
  }

  @Test
  fun `must return contacts performed by other people than the person manager and outside the team`() {

    wmtcmsRepository.save(WMTCMSEntity(personManagerStaffCode = staffIdentifier.staffCode, personManagerTeamCode = staffIdentifier.teamCode, staffCode = "OTHERSTAFFCODE", staffTeamCode = "TM2", contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsInCaseloadPerformedByOthers(staffIdentifier)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(contactTypeCode, results[0].typeCode)
  }

  @Test
  fun `must return contacts performed by other people than the person manager and in the same team`() {

    wmtcmsRepository.save(WMTCMSEntity(personManagerStaffCode = staffIdentifier.staffCode, personManagerTeamCode = staffIdentifier.teamCode, staffCode = "OTHERSTAFFCODE", staffTeamCode = staffIdentifier.teamCode, contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsInCaseloadPerformedByOthers(staffIdentifier)

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(contactTypeCode, results[0].typeCode)
  }

  @Test
  fun `must return empty list when no cms entries exist and getting contacts in caseload performed by others`() {

    val results = wmtGetContacts.findContactsInCaseloadPerformedByOthers(staffIdentifier)

    Assertions.assertTrue(results.isEmpty())
  }

  @Test
  fun `must not return contacts in caseload performed by others if its the same person performing the contact and is the person manager`() {

    wmtcmsRepository.save(WMTCMSEntity(staffCode = staffIdentifier.staffCode, staffTeamCode = staffIdentifier.teamCode, personManagerStaffCode = staffIdentifier.staffCode, personManagerTeamCode = staffIdentifier.teamCode, contactTypeCode = contactTypeCode))

    val results = wmtGetContacts.findContactsInCaseloadPerformedByOthers(staffIdentifier)

    Assertions.assertTrue(results.isEmpty())
  }
}
