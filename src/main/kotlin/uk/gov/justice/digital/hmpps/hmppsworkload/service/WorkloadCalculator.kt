package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import java.math.BigInteger

interface WorkloadCalculator {

  fun getWorkloadPoints(cases: List<Case>, courtReports: List<CourtReport>): BigInteger
}
