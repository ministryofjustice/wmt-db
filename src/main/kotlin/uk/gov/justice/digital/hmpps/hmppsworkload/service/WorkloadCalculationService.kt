package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
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
import java.math.BigInteger

@Service
class WorkloadCalculationService(
  private val workloadCalculator: WorkloadCalculator,
  private val getCourtReports: WMTGetCourtReports,
  private val getContacts: WMTGetContacts,
  private val getContactTypeWeightings: GetContactTypeWeightings,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val weeklyHours: GetWeeklyHours,
  private val getReductionService: GetReductionService,
  private val workloadCalculationRepository: WorkloadCalculationRepository,
  private val getCaseLoad: GetCombinedCaseload,
) {

  fun saveWorkloadCalculation(
    staffIdentifier: StaffIdentifier,
    staffGrade: String,
    availableHours: BigDecimal = getAvailableHours(
      staffIdentifier,
      staffGrade,
    ),
  ): WorkloadCalculationEntity {
    return workloadCalculationRepository.save(calculate(staffIdentifier, staffGrade, availableHours))
  }

  private fun getAvailableHours(
    staffIdentifier: StaffIdentifier,
    staffGrade: String,
  ): BigDecimal {
    return this.weeklyHours.findWeeklyHours(staffIdentifier, staffGrade) - getReductionService.findReductionHours(
      staffIdentifier,
    )
  }

  private fun calculate(staffIdentifier: StaffIdentifier, staffGrade: String, availableHours: BigDecimal): WorkloadCalculationEntity {
    val workloadCalculationsElements = getWorkloadCalculationElements(staffIdentifier)

    return WorkloadCalculationEntity(
      availablePoints = calculateAvailablePoints(workloadCalculationsElements.workloadPointsWeighting.getDefaultPointsAvailable(staffGrade), workloadCalculationsElements.workloadPointsWeighting.getDefaultContractedHours(staffGrade), availableHours),
      workloadPoints = workloadCalculationsElements.workloadPoints,
      staffCode = staffIdentifier.staffCode,
      teamCode = staffIdentifier.teamCode,
      breakdownData = BreakdownDataEntity(
        getCourtReportCounts(workloadCalculationsElements.courtReports, CourtReportType.STANDARD),
        getCourtReportCounts(workloadCalculationsElements.courtReports, CourtReportType.FAST),
        getContactTypeCodeCounts(workloadCalculationsElements.contactsPerformedOutsideCaseload),
        getContactTypeCodeCounts(workloadCalculationsElements.contactsPerformedByOthers),
        workloadCalculationsElements.contactTypeWeightings,
        workloadCalculationsElements.cases.size,
        availableHours,
      ),
    )
  }

  private fun getWorkloadCalculationElements(staffIdentifier: StaffIdentifier): WorkloadCalculationElements {
    val cases = getCaseLoad.getCases(staffIdentifier)
    val courtReports = getCourtReports.getCourtReports(staffIdentifier)
    val contactsPerformedOutsideCaseload = getContacts.findContactsOutsideCaseload(staffIdentifier)
    val contactsPerformedByOthers = getContacts.findContactsInCaseloadPerformedByOthers(staffIdentifier)
    val contactTypeWeightings = getContactTypeWeightings.findAll()
    val t2aWorkloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true)
    val workloadPointsWeighting = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val workloadPoints = workloadCalculator.getWorkloadPoints(
      cases,
      courtReports,
      contactsPerformedOutsideCaseload,
      contactsPerformedByOthers,
      contactTypeWeightings,
      t2aWorkloadPoints,
      workloadPointsWeighting,
    )
    return WorkloadCalculationElements(
      cases,
      courtReports,
      contactsPerformedOutsideCaseload,
      contactsPerformedByOthers,
      contactTypeWeightings,
      t2aWorkloadPoints,
      workloadPointsWeighting,
      workloadPoints,
    )
  }
  private fun getCourtReportCounts(courtReports: List<CourtReport>, type: CourtReportType): Int =
    courtReports.count { it.type == type }

  private fun getContactTypeCodeCounts(contacts: List<Contact>): Map<String, Int> =
    contacts.groupingBy { it.typeCode }.eachCount()
}

data class WorkloadCalculationElements(
  val cases: List<Case>,
  val courtReports: List<CourtReport>,
  val contactsPerformedOutsideCaseload: List<Contact>,
  val contactsPerformedByOthers: List<Contact>,
  val contactTypeWeightings: Map<String, Int>,
  val t2aWorkloadPoints: WorkloadPointsEntity,
  val workloadPointsWeighting: WorkloadPointsEntity,
  val workloadPoints: BigInteger,
)
