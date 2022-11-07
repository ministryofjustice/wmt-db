package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.ImpactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerCases
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.CapacityCalculator
import uk.gov.justice.digital.hmpps.hmppsworkload.service.CaseCalculator
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetSentenceService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetWeeklyHours
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

@Service
class GetOffenderManagerService(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val capacityCalculator: CapacityCalculator,
  private val caseCalculator: CaseCalculator,
  private val getReductionService: GetReductionService,
  private val communityApiClient: CommunityApiClient,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val getSentenceService: GetSentenceService,
  private val caseDetailsRepository: CaseDetailsRepository,
  private val getWeeklyHours: GetWeeklyHours,
  private val getEventManager: JpaBasedGetEventManager
) {

  fun getPotentialWorkload(staffCode: String, teamCode: String, impactCase: ImpactCase): OffenderManagerOverview? {
    return getOverview(staffCode, teamCode)?.let { overview ->
      val currentCaseImpact = getCurrentCasePoints(teamCode, overview.code, impactCase.crn)
      overview.potentialCapacity = capacityCalculator.calculate(
        overview.totalPoints.minus(currentCaseImpact)
          .plus(caseCalculator.getPointsForCase(getPotentialCase(crn = impactCase.crn))),
        overview.availablePoints
      )
      overview
    }
  }

  private fun getPotentialCase(crn: String): Case {
    return caseDetailsRepository.findByIdOrNull(crn)!!
      .let { Case(tier = it.tier, type = it.type, crn = crn) }
  }

  private fun getCurrentCasePoints(teamCode: String, staffCode: String, crn: String): BigInteger = offenderManagerRepository.findCaseByTeamCodeAndStaffCodeAndCrn(teamCode, staffCode, crn)?.let { currentCase ->
    return caseCalculator.getPointsForCase(Case(Tier.valueOf(currentCase.tier), CaseType.valueOf(currentCase.caseCategory), false, crn))
  } ?: BigInteger.ZERO

  private fun getDefaultOffenderManagerOverview(
    deliusStaff: DeliusStaff,
    teamName: String
  ): OffenderManagerOverview {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val availablePoints = workloadPoints.getDefaultPointsAvailable(deliusStaff.grade)
    val defaultContractedHours = workloadPoints.getDefaultContractedHours(deliusStaff.grade)

    val overview = OffenderManagerOverview(deliusStaff.staff.forenames, deliusStaff.staff.surname, 0, 0, availablePoints.toBigInteger(), BigInteger.ZERO, deliusStaff.staffCode, teamName, LocalDateTime.now(), -1, BigInteger.ZERO)
    overview.capacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
    overview.contractedHours = defaultContractedHours
    return overview
  }

  fun getOverview(offenderManagerCode: String, teamCode: String): OffenderManagerOverview? =
    communityApiClient.getStaffByCode(offenderManagerCode).map { staff ->
      val team = staff.teams!!.first { team -> team.code == teamCode }
      val overview = offenderManagerRepository.findByOverview(team.code, staff.staffCode)?.let {
        it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
        it.nextReductionChange = getReductionService.findNextReductionChange(offenderManagerCode, teamCode)
        it.reductionHours = getReductionService.findReductionHours(offenderManagerCode, teamCode)
        it.contractedHours = getWeeklyHours.findWeeklyHours(offenderManagerCode, teamCode, staff.grade)
        offenderManagerRepository.findByCaseloadTotals(it.workloadOwnerId).let { totals ->
          it.tierCaseTotals = totals.map { total -> TierCaseTotals(total.getATotal(), total.getBTotal(), total.getCTotal(), total.getDTotal(), total.untiered) }
            .fold(TierCaseTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)) { first, second -> TierCaseTotals(first.A.add(second.A), first.B.add(second.B), first.C.add(second.C), first.D.add(second.D), first.untiered.add(second.untiered)) }
        }
        offenderManagerRepository.findCasesByTeamCodeAndStaffCode(offenderManagerCode, teamCode).let { cases ->
          val crns = cases.map { case -> case.crn }
          it.caseEndDue = getSentenceService.getCasesDueToEndCount(crns)
          it.releasesDue = getSentenceService.getCasesDueToBeReleases(crns)
        }
        it
      } ?: getDefaultOffenderManagerOverview(staff, team.description)
      overview.grade = staff.grade
      overview.lastAllocatedEvent = getEventManager.findLatestByStaffAndTeam(offenderManagerCode, teamCode)
      overview
    }.block()

  fun getCases(teamCode: String, offenderManagerCode: String): OffenderManagerCases? =
    offenderManagerRepository.findCasesByTeamCodeAndStaffCode(offenderManagerCode, teamCode).let { cases ->
      val crnDetails = getCrnToCaseDetails(cases.map { it.crn })
      communityApiClient.getStaffByCode(offenderManagerCode)
        .map { staffSummary ->
          val team = staffSummary.teams?.first { team -> team.code == teamCode }
          OffenderManagerCases.from(staffSummary, team!!, cases, crnDetails)
        }.block()
    }

  private fun getCrnToCaseDetails(crns: List<String>): Map<String, CaseDetailsEntity> {
    return if (crns.isEmpty()) emptyMap() else caseDetailsRepository.findAllById(crns).associateBy { it.crn }
  }
}
