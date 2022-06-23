package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import java.math.BigInteger

interface WorkloadCalculator {

  fun getWorkloadPoints(
    cases: List<Case>,
    courtReports: List<CourtReport>,
    institutionalReports: Int,
    assessments: List<Assessment>,
    contacts: List<Contact>,
    contactTypeWeightings: Map<String, BigInteger>,
    t2aWorkloadPoints: WorkloadPointsEntity,
    workloadPoints: WorkloadPointsEntity
  ): BigInteger
}
