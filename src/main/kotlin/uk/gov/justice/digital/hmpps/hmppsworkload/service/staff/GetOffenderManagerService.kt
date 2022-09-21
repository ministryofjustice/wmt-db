package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.ImpactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerCases
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview

interface GetOffenderManagerService {

  fun getPotentialWorkload(staffCode: String, teamCode: String, impactCase: ImpactCase): OffenderManagerOverview?
  fun getOverview(offenderManagerCode: String, teamCode: String): OffenderManagerOverview?
  fun getCases(teamCode: String, offenderManagerCode: String): OffenderManagerCases?
}
