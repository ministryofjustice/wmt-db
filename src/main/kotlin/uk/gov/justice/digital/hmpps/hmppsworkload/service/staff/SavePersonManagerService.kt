package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity

interface SavePersonManagerService {

  fun savePersonManager(teamCode: String, deliusStaff: DeliusStaff, loggedInUser: String, personSummary: PersonSummary, crn: String): SaveResult<PersonManagerEntity>
}
