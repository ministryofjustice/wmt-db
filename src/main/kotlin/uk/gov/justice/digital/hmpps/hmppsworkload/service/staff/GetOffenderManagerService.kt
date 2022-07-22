package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.ImpactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerCases
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import java.math.BigInteger

interface GetOffenderManagerService {

  fun getPotentialWorkload(teamCode: String, staffId: BigInteger, impactCase: ImpactCase): OffenderManagerOverview?
  fun getPotentialWorkload(teamCode: String, staffCode: String, impactCase: ImpactCase): OffenderManagerOverview?
  fun getOverview(teamCode: String, offenderManagerCode: String): OffenderManagerOverview?
  fun getCases(teamCode: String, offenderManagerCode: String): OffenderManagerCases?
}
