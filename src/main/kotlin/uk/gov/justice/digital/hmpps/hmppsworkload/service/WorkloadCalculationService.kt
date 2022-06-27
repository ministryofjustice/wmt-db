package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.BreakdownDataEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadCalculationRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository

@Service
class WorkloadCalculationService(
  private val workloadCalculator: WorkloadCalculator,
  private val getCourtReports: GetCourtReports,
  private val getParoleReports: GetParoleReports,
  private val getAssessments: GetAssessments,
  private val getContacts: GetContacts,
  private val getContactTypeWeightings: GetContactTypeWeightings,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val weeklyHours: GetWeeklyHours,
  private val getReductionService: GetReductionService,
  private val capacityCalculator: CapacityCalculator,
  private val workloadCalculationRepository: WorkloadCalculationRepository
) {

  fun calculate(staffCode: String, teamCode: String, providerCode: String, staffGrade: String): WorkloadCalculationEntity {

    val cases = emptyList<Case>()
    val courtReports = getCourtReports.getCourtReports(staffCode, teamCode)
    val paroleReports = getParoleReports.getParoleReports(staffCode, teamCode)
    val assessments = getAssessments.getAssessments(staffCode, teamCode)
    val contactsPerformedOutsideCaseload = getContacts.findContactsOutsideCaseload(staffCode, teamCode)
    val contactsPerformedByOthers = getContacts.findContactsInCaseloadPerformedByOthers(staffCode, teamCode)
    val contactTypeWeightings = getContactTypeWeightings.findAll()
    val t2aWorkloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true)
    val workloadPointsWeighting = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val weeklyHours = weeklyHours.findWeeklyHours(teamCode, staffCode, staffGrade)
    val reductions = getReductionService.findReductionHours(staffCode, teamCode)
    val availablePoints = capacityCalculator.calculateAvailablePoints(workloadPointsWeighting.getDefaultPointsAvailable(staffGrade), weeklyHours, reductions, workloadPointsWeighting.getDefaultContractedHours(staffGrade))
    val workloadPoints = workloadCalculator.getWorkloadPoints(cases, courtReports, paroleReports, assessments, contactsPerformedOutsideCaseload, contactsPerformedByOthers, contactTypeWeightings, t2aWorkloadPoints, workloadPointsWeighting)

    return workloadCalculationRepository.save(
      WorkloadCalculationEntity(
        weeklyHours = weeklyHours, reductions = reductions, availablePoints = availablePoints, workloadPoints = workloadPoints, staffCode = staffCode, teamCode = teamCode, providerCode = providerCode, breakdownData = BreakdownDataEntity(0)
      )
    )
  }
}
