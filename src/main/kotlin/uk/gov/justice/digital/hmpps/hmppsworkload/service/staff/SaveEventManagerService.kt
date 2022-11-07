package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity

interface SaveEventManagerService {
  fun saveEventManager(teamCode: String, deliusStaff: DeliusStaff, allocateCase: AllocateCase, loggedInUser: String): SaveResult<EventManagerEntity>
}
