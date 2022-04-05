package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.ImpactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerCases
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PotentialCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.CaseTypeMapper
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class JpaBasedGetOffenderManagerService(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val capacityCalculator: CapacityCalculator,
  private val caseCalculator: CaseCalculator,
  private val reductionsRepository: ReductionsRepository,
  private val communityApiClient: CommunityApiClient,
  private val gradeMapper: GradeMapper,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val caseTypeMapper: CaseTypeMapper,
  private val hmppsTierApiClient: HmppsTierApiClient
) : GetOffenderManagerService {

  override fun getPotentialWorkload(teamCode: String, staffId: BigInteger, impactCase: ImpactCase): OffenderManagerOverview? {
    return Mono.zip(getOffenderManagerOverview(staffId, teamCode), getPotentialCase(impactCase.crn, impactCase.convictionId))
      .map { results ->
        results.t1.potentialCapacity = capacityCalculator.calculate(results.t1.totalPoints.plus(caseCalculator.getPointsForCase(results.t2)), results.t1.availablePoints)
        results.t1
      }.block()
  }

  private fun getPotentialCase(crn: String, convictionId: Long): Mono<PotentialCase> {
    return Mono.zip(communityApiClient.getActiveConvictions(crn), hmppsTierApiClient.getTierByCrn(crn))
      .map { results ->
        val caseType = caseTypeMapper.getCaseType(results.t1, convictionId)
        val tier = results.t2
        PotentialCase(Tier.valueOf(tier), caseType, false)
      }
  }

  private fun getOffenderManagerOverview(
    staffId: BigInteger,
    teamCode: String
  ) = communityApiClient.getStaffById(staffId)
    .map { staff ->
      val overview = offenderManagerRepository.findByOverview(teamCode, staff.staffCode)?.let {
        it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
        it.grade = gradeMapper.workloadToStaffGrade(it.grade)
        it
      } ?: run {
        getDefaultOffenderManagerOverview(
          staff.staff.forenames,
          staff.staff.surname,
          gradeMapper.deliusToStaffGrade(staff.staffGrade?.code),
          staff.staffCode
        )
      }
      overview.potentialCapacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
      overview
    }

  fun getDefaultOffenderManagerOverview(forename: String, surname: String, grade: String, staffCode: String): OffenderManagerOverview {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val defaultAvailablePointsForGrade = getDefaultPointsAvailable(workloadPoints, grade)
    val defaultContractedHours = getDefaultContractedHours(workloadPoints, grade)
    val availablePoints = capacityCalculator.calculateAvailablePoints(
      defaultAvailablePointsForGrade, defaultContractedHours,
      BigDecimal.ZERO, defaultContractedHours
    )
    val overview = OffenderManagerOverview(forename, surname, grade, BigDecimal.ZERO, BigDecimal.ZERO, availablePoints.toBigInteger(), BigInteger.ZERO, staffCode, "", BigDecimal.ZERO, defaultContractedHours, null, -1)
    overview.capacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
    return overview
  }

  fun getDefaultPointsAvailable(workloadPoints: WorkloadPointsEntity, grade: String): BigDecimal {
    return when (grade) {
      "SPO" -> workloadPoints.defaultAvailablePointsSPO
      else -> workloadPoints.defaultAvailablePointsPO
    }
  }

  fun getDefaultContractedHours(workloadPoints: WorkloadPointsEntity, grade: String): BigDecimal {
    return when (grade) {
      "PO", "PQiP" -> workloadPoints.defaultContractedHoursPO
      "PSO" -> workloadPoints.defaultContractedHoursPSO
      else -> workloadPoints.defaultContractedHoursSPO
    }
  }

  override fun getOverview(teamCode: String, offenderManagerCode: String): OffenderManagerOverview? = offenderManagerRepository.findByOverview(teamCode, offenderManagerCode)?.let {
    it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
    reductionsRepository.findByStatusIsInAndWorkloadOwnerIdIs(listOf(ReductionStatus.ACTIVE, ReductionStatus.SCHEDULED), it.workloadOwnerId).let { reductions ->
      reductions.flatMap { reduction -> listOf(reduction.effectiveFrom, reduction.effectiveTo ?: ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault())) }
        .filter { date -> !date.isBefore(ZonedDateTime.now()) }
        .minByOrNull { date -> date }
        ?.let { nextReductionChange -> it.nextReductionChange = nextReductionChange }
    }
    offenderManagerRepository.findByCaseloadTotals(it.workloadOwnerId).let { totals ->
      it.tierCaseTotals = totals.map { total -> TierCaseTotals(total.getATotal(), total.getBTotal(), total.getCTotal(), total.getDTotal(), total.untiered) }
        .fold(TierCaseTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)) { first, second -> TierCaseTotals(first.A.add(second.A), first.B.add(second.B), first.C.add(second.C), first.D.add(second.D), first.untiered.add(second.untiered)) }
    }
    return it
  }

  override fun getCases(teamCode: String, offenderManagerCode: String): OffenderManagerCases? =
    communityApiClient.getStaffByCode(offenderManagerCode)
      .map { staff ->
        val cases = offenderManagerRepository.findCasesByTeamCodeAndStaffCode(teamCode, offenderManagerCode)
        val team = staff.teams?.first { team -> team.code == teamCode }
        OffenderManagerCases.from(staff, gradeMapper.deliusToStaffGrade(staff.staffGrade?.code), team!!)
      }.block()
}
