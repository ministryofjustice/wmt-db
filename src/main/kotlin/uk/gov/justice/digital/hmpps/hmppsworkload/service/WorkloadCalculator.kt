package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReport
import java.math.BigInteger

interface WorkloadCalculator {

  fun getWorkloadPoints(cases: List<Case>, courtReports: List<CourtReport>, institutionalReports: List<InstitutionalReport>, assessments: List<Assessment>): BigInteger
}
