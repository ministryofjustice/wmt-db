package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import java.math.BigInteger

interface CaseCalculator {

  fun getPointsForCase(potentialCase: Case): BigInteger
}
