package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity

interface SaveEventManagerService {
  fun saveEventManager(teamCode: String, staff: Staff, allocateCase: AllocateCase, loggedInUser: String): EventManagerEntity
}
