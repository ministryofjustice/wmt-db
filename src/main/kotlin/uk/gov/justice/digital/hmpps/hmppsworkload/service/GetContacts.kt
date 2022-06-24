package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Contact

interface GetContacts {

  fun findContactsOutsideCaseload(staffCode: String, teamCode: String): List<Contact>

  fun findContactsInCaseloadPerformedByOthers(staffCode: String, teamCode: String): List<Contact>
}
