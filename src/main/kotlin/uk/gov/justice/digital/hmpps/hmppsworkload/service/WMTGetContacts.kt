package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCMSRepository

@Service
class WMTGetContacts(private val wmtcmsRepository: WMTCMSRepository) {

  fun findContactsOutsideCaseload(staffIdentifier: StaffIdentifier): List<Contact> = wmtcmsRepository.findByStaffTeamCodeAndStaffCodeAndPersonManagerStaffCodeNot(staffIdentifier.teamCode, staffIdentifier.staffCode, staffIdentifier.staffCode)
    .map { Contact(it.contactTypeCode) }

  fun findContactsInCaseloadPerformedByOthers(staffIdentifier: StaffIdentifier): List<Contact> = wmtcmsRepository.findByPersonManagerTeamCodeAndPersonManagerStaffCodeAndStaffCodeNot(staffIdentifier.teamCode, staffIdentifier.staffCode, staffIdentifier.staffCode)
    .map { Contact(it.contactTypeCode) }
}
