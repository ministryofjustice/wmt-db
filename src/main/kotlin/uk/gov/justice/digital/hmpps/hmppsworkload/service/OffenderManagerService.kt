package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.ImpactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PotentialCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview

interface OffenderManagerService {
  fun getPotentialWorkload(
    teamCode: String,
    offenderManagerCode: String,
    potentialCase: PotentialCase
  ): OffenderManagerOverview?

  fun getPotentialWorkload(teamCode: String, staffId: Long, impactCase: ImpactCase): OffenderManagerOverview?
  fun getOverview(teamCode: String, offenderManagerCode: String): OffenderManagerOverview?
}
