package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCMSRepository

@Service
class WMTGetContacts(private val wmtcmsRepository: WMTCMSRepository) : GetContacts {

  override fun findContactsOutsideCaseload(staffCode: String, teamCode: String): List<Contact> =
    wmtcmsRepository.findByStaffTeamCodeAndStaffCodeAndPersonManagerStaffCodeNot(teamCode, staffCode, staffCode)
      .map { Contact(it.contactTypeCode) }

  override fun findContactsInCaseloadPerformedByOthers(staffCode: String, teamCode: String): List<Contact> =
    wmtcmsRepository.findByPersonManagerTeamCodeAndPersonManagerStaffCodeAndStaffCodeNot(teamCode, staffCode, staffCode)
      .map { Contact(it.contactTypeCode) }
}
