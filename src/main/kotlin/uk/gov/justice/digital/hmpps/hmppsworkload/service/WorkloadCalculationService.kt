package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Assessment
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReportType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.BreakdownDataEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadCalculationRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService
import java.math.BigDecimal

@Service
class WorkloadCalculationService(
  private val workloadCalculator: WorkloadCalculator,
  private val getCourtReports: WMTGetCourtReports,
  private val getParoleReports: GetParoleReports,
  private val getAssessments: GetAssessments,
  private val getContacts: GetContacts,
  private val getContactTypeWeightings: GetContactTypeWeightings,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val weeklyHours: GetWeeklyHours,
  private val getReductionService: GetReductionService,
  private val capacityCalculator: CapacityCalculator,
  private val workloadCalculationRepository: WorkloadCalculationRepository,
  private val getCaseLoad: GetCombinedCaseload
) {

  fun calculate(staffCode: String, teamCode: String, staffGrade: String): WorkloadCalculationEntity {
    val weeklyHours = weeklyHours.findWeeklyHours(staffCode, teamCode, staffGrade)
    val reductions = getReductionService.findReductionHours(staffCode, teamCode)
    val availableHours = weeklyHours - reductions
    return calculate(staffCode, teamCode, staffGrade, availableHours)
  }

  fun calculate(staffCode: String, teamCode: String, staffGrade: String, availableHours: BigDecimal): WorkloadCalculationEntity {
    val staffIdentifier = StaffIdentifier(staffCode, teamCode)
    val cases = getCaseLoad.getCases(staffIdentifier)
    val courtReports = getCourtReports.getCourtReports(staffIdentifier)
    val paroleReports = getParoleReports.getParoleReports(staffCode, teamCode)
    val assessments = getAssessments.getAssessments(staffCode, teamCode)
    val contactsPerformedOutsideCaseload = getContacts.findContactsOutsideCaseload(staffCode, teamCode)
    val contactsPerformedByOthers = getContacts.findContactsInCaseloadPerformedByOthers(staffCode, teamCode)
    val contactTypeWeightings = getContactTypeWeightings.findAll()
    val t2aWorkloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true)
    val workloadPointsWeighting: WorkloadPointsEntity = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val availablePoints = capacityCalculator.calculateAvailablePoints(
      workloadPointsWeighting.getDefaultPointsAvailable(staffGrade),
      workloadPointsWeighting.getDefaultContractedHours(staffGrade),
      availableHours
    )
    val workloadPoints = workloadCalculator.getWorkloadPoints(cases, courtReports, paroleReports, assessments, contactsPerformedOutsideCaseload, contactsPerformedByOthers, contactTypeWeightings, t2aWorkloadPoints, workloadPointsWeighting)
    return workloadCalculationRepository.save(WorkloadCalculationEntity(availablePoints = availablePoints, workloadPoints = workloadPoints, staffCode = staffCode, teamCode = teamCode, breakdownData = BreakdownDataEntity(getCourtReportCounts(courtReports, CourtReportType.STANDARD), getCourtReportCounts(courtReports, CourtReportType.FAST), paroleReports, getAssessmentCounts(assessments, CaseType.COMMUNITY), getAssessmentCounts(assessments, CaseType.LICENSE), getContactTypeCodeCounts(contactsPerformedOutsideCaseload), getContactTypeCodeCounts(contactsPerformedByOthers), contactTypeWeightings, cases.size, availableHours)))
  }

  private fun getCourtReportCounts(courtReports: List<CourtReport>, type: CourtReportType): Int =
    courtReports.count { it.type == type }

  private fun getAssessmentCounts(assessments: List<Assessment>, type: CaseType): Int =
    assessments.count { it.category == type }

  private fun getContactTypeCodeCounts(contacts: List<Contact>): Map<String, Int> =
    contacts.groupingBy { c -> c.typeCode }.eachCount()
}
