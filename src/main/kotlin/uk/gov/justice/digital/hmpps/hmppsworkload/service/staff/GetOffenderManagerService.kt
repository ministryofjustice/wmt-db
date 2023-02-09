package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerCases
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerPotentialWorkload
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.CaseCalculator
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetSentenceService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetWeeklyHours
import uk.gov.justice.digital.hmpps.hmppsworkload.service.calculateCapacity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

@Service
class GetOffenderManagerService(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val caseCalculator: CaseCalculator,
  private val getReductionService: GetReductionService,
  private val communityApiClient: CommunityApiClient,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val getSentenceService: GetSentenceService,
  private val caseDetailsRepository: CaseDetailsRepository,
  private val getWeeklyHours: GetWeeklyHours,
  private val getEventManager: JpaBasedGetEventManager,
  private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient
) {

  fun getPotentialWorkload(staffIdentifier: StaffIdentifier, crn: String): OffenderManagerPotentialWorkload? {
    return workforceAllocationsToDeliusApiClient.impact(crn, staffIdentifier.staffCode).map { impactResponse ->
      val overview = findOffenderManagerOverview(staffIdentifier, impactResponse.staff.getGrade())
      overview.forename = impactResponse.staff.name.forename
      overview.surname = impactResponse.staff.name.surname
      val currentCaseImpact = getCurrentCasePoints(staffIdentifier, crn)
      val potentialCase = getPotentialCase(crn)
      overview.potentialCapacity = calculateCapacity(
        overview.totalPoints.minus(currentCaseImpact)
          .plus(caseCalculator.getPointsForCase(potentialCase)),
        overview.availablePoints
      )
      OffenderManagerPotentialWorkload.from(overview, impactResponse, potentialCase)
    }.block()
  }

  private fun getPotentialCase(crn: String): Case {
    return caseDetailsRepository.findByIdOrNull(crn)!!
      .let { Case(tier = it.tier, type = it.type, crn = crn) }
  }

  private fun getCurrentCasePoints(staffIdentifier: StaffIdentifier, crn: String): BigInteger = offenderManagerRepository.findCaseByTeamCodeAndStaffCodeAndCrn(staffIdentifier.teamCode, staffIdentifier.staffCode, crn)?.let { currentCase ->
    return caseCalculator.getPointsForCase(Case(Tier.valueOf(currentCase.tier), CaseType.valueOf(currentCase.caseCategory), false, crn))
  } ?: BigInteger.ZERO

  private fun getDefaultOffenderManagerOverview(staffCode: String, grade: String): OffenderManagerOverview {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val availablePoints = workloadPoints.getDefaultPointsAvailable(grade)
    val defaultContractedHours = workloadPoints.getDefaultContractedHours(grade)

    val overview = OffenderManagerOverview(0, 0, availablePoints.toBigInteger(), BigInteger.ZERO, staffCode, LocalDateTime.now(), -1, BigInteger.ZERO)
    overview.capacity = calculateCapacity(overview.totalPoints, overview.availablePoints)
    overview.contractedHours = defaultContractedHours
    overview.grade = grade
    return overview
  }

  fun getOverview(staffIdentifier: StaffIdentifier): OffenderManagerOverview? {
    val officerView = workforceAllocationsToDeliusApiClient.getOfficerView(staffIdentifier.staffCode).block()
    val overview = findOffenderManagerOverview(staffIdentifier, officerView.staffGrade)
    overview.caseEndDue = officerView.casesDueToEndInNext4Weeks
    overview.releasesDue = officerView.releasesWithinNext4Weeks
    overview.forename = officerView.name.forename
    overview.surname = officerView.name.surname
    overview.grade = officerView.staffGrade
    overview.email = officerView.email
    overview.lastAllocatedEvent = getEventManager.findLatestByStaffAndTeam(staffIdentifier)

    if (overview.hasWorkload) {
      overview.nextReductionChange = getReductionService.findNextReductionChange(staffIdentifier)
      overview.reductionHours = getReductionService.findReductionHours(staffIdentifier)
      overview.contractedHours = getWeeklyHours.findWeeklyHours(staffIdentifier, overview.grade)
      offenderManagerRepository.findByCaseloadTotals(overview.workloadOwnerId).let { totals ->
        overview.tierCaseTotals = totals.map { total ->
          TierCaseTotals(total.getATotal(), total.getBTotal(), total.getCTotal(), total.getDTotal(), total.untiered)
        }
          .fold(TierCaseTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)) { first, second -> TierCaseTotals(first.A.add(second.A), first.B.add(second.B), first.C.add(second.C), first.D.add(second.D), first.untiered.add(second.untiered)) }
      }
    }
    return overview
  }

  fun findOffenderManagerOverview(staffIdentifier: StaffIdentifier, grade: String): OffenderManagerOverview = offenderManagerRepository.findByOverview(staffIdentifier.teamCode, staffIdentifier.staffCode)?.let {
    it.capacity = calculateCapacity(it.totalPoints, it.availablePoints)
    it.grade = grade
    it.hasWorkload = true
    it
  } ?: getDefaultOffenderManagerOverview(staffIdentifier.staffCode, grade)

  fun getCases(staffIdentifier: StaffIdentifier): OffenderManagerCases? =
    offenderManagerRepository.findCasesByTeamCodeAndStaffCode(staffIdentifier.staffCode, staffIdentifier.teamCode).let { cases ->
      val crnDetails = getCrnToCaseDetails(cases.map { it.crn })
      communityApiClient.getStaffByCode(staffIdentifier.staffCode)
        .map { staffSummary ->
          val team = staffSummary.teams?.first { team -> team.code == staffIdentifier.teamCode }
          OffenderManagerCases.from(staffSummary, team!!, cases, crnDetails)
        }.block()
    }

  private fun getCrnToCaseDetails(crns: List<String>): Map<String, CaseDetailsEntity> {
    return if (crns.isEmpty()) emptyMap() else caseDetailsRepository.findAllById(crns).associateBy { it.crn }
  }
}
