package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import java.math.BigInteger

class DefaultWorkloadCalculator(private val workloadPointsRepository: WorkloadPointsRepository) : WorkloadCalculator {
  override fun getWorkloadPoints(cases: List<Case>, courtReports: List<CourtReport>): BigInteger {
    val t2aWorkloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true)
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)

    val casePointTotal = cases.map { case ->
      var caseWorkloadPoints = workloadPoints
      if (case.isT2A) {
        caseWorkloadPoints = t2aWorkloadPoints
      }
      val tierWeightings = caseWorkloadPoints.getTierPointsMap(case.type)
      tierWeightings[case.tier]!!
    }.fold(BigInteger.ZERO) { first, second -> first.add(second) }

    val courtReportTotal = courtReports.map { courtReport ->
      when (courtReport.type) {
        CourtReportType.FAST -> workloadPoints.fastCourtReportPoints
        CourtReportType.STANDARD -> workloadPoints.standardCourtReportPoints
      }
    }.fold(BigInteger.ZERO) { first, second -> first.add(second) }
    return casePointTotal.add(courtReportTotal)
  }
}
