package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.SaveResult
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity

interface SaveRequirementManagerService {
  fun saveRequirementManagers(teamCode: String, deliusStaff: DeliusStaff, allocateCase: AllocateCase, loggedInUser: String, requirements: List<ConvictionRequirement>): List<SaveResult<RequirementManagerEntity>>
}
