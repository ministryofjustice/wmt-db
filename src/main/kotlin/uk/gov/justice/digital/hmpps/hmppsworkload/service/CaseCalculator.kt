package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PotentialCase
import java.math.BigInteger

interface CaseCalculator {

  fun getPointsForCase(potentialCase: PotentialCase): BigInteger
}
