package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import java.math.BigInteger

class DefaultWorkloadCalculator : WorkloadCalculator {
  override fun getWorkloadPoints(
    cases: List<Case>,
    courtReports: List<CourtReport>,
    institutionalReports: List<InstitutionalReport>,
    assessments: List<Assessment>,
    contacts: List<Contact>,
    contactTypeWeightings: Map<String, BigInteger>,
    t2aWorkloadPoints: WorkloadPointsEntity,
    workloadPoints: WorkloadPointsEntity
  ): BigInteger {

    val casePointTotal = calculateCaseTierPointsTotal(cases, t2aWorkloadPoints, workloadPoints)
    val courtReportTotal = calculateCourtReportPointsTotal(courtReports, workloadPoints)
    val institutionalReportTotal = calculateInstitutionalReportPointsTotal(institutionalReports, workloadPoints)
    val assessmentTotal = calculateAssessmentPointsTotal(assessments, workloadPoints)
    val contactTotal = calculateContactPointsTotal(contacts, contactTypeWeightings, cases.map { it.crn }.toSet())

    return casePointTotal.add(courtReportTotal).add(institutionalReportTotal).add(assessmentTotal).add(contactTotal)
  }

  private fun calculateCaseTierPointsTotal(cases: List<Case>, t2aWorkloadPoints: WorkloadPointsEntity, workloadPoints: WorkloadPointsEntity): BigInteger = cases.map { case ->
    var caseWorkloadPoints = workloadPoints
    if (case.isT2A) {
      caseWorkloadPoints = t2aWorkloadPoints
    }
    val tierWeightings = caseWorkloadPoints.getTierPointsMap(case.type)
    tierWeightings[case.tier]!!
  }.fold(BigInteger.ZERO) { first, second -> first.add(second) }

  private fun calculateCourtReportPointsTotal(courtReports: List<CourtReport>, workloadPoints: WorkloadPointsEntity): BigInteger = courtReports.map { courtReport ->
    when (courtReport.type) {
      CourtReportType.FAST -> workloadPoints.fastCourtReportPoints
      CourtReportType.STANDARD -> workloadPoints.standardCourtReportPoints
    }
  }.fold(BigInteger.ZERO) { first, second -> first.add(second) }

  private fun calculateInstitutionalReportPointsTotal(institutionalReports: List<InstitutionalReport>, workloadPoints: WorkloadPointsEntity): BigInteger =
    when (workloadPoints.paroleReportWeightingEnabled) {
      true -> {
        institutionalReports.map { institutionalReport ->
          when (institutionalReport.type) {
            InstitutionalReportType.PAROLE_REPORT -> workloadPoints.paroleReportWeighting
            InstitutionalReportType.OTHER -> BigInteger.ZERO
          }
        }.fold(BigInteger.ZERO) { first, second -> first.add(second) }
      }
      false -> BigInteger.ZERO
    }

  private fun calculateAssessmentPointsTotal(assessments: List<Assessment>, workloadPoints: WorkloadPointsEntity): BigInteger = assessments.map { assessment ->
    when (assessment.category) {
      CaseType.COMMUNITY -> workloadPoints.communityARMAssessmentWeighting
      CaseType.LICENSE -> workloadPoints.licenseARMAssessmentWeighting
      else -> BigInteger.ZERO
    }
  }.fold(BigInteger.ZERO) { first, second -> first.add(second) }

  private fun calculateContactPointsTotal(contacts: List<Contact>, contactTypeWeightings: Map<String, BigInteger>, managedCrns: Set<String>): BigInteger = contacts.map { contact ->
    if (managedCrns.contains(contact.crn)) {
      BigInteger.ZERO
    } else {
      contactTypeWeightings.getOrDefault(contact.typeCode, BigInteger.ZERO)
    }
  }.fold(BigInteger.ZERO) { first, second -> first.add(second) }
}
