package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.OffenderSearchApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.ImpactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerCases
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.TierCaseTotals
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Service
class JpaBasedGetOffenderManagerService(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val capacityCalculator: CapacityCalculator,
  private val caseCalculator: CaseCalculator,
  private val reductionsRepository: ReductionsRepository,
  private val communityApiClient: CommunityApiClient,
  private val gradeMapper: GradeMapper,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val offenderSearchApiClient: OffenderSearchApiClient,
  private val sentenceRepository: SentenceRepository,
  private val caseDetailsRepository: CaseDetailsRepository
) : GetOffenderManagerService {

  override fun getPotentialWorkload(teamCode: String, staffId: BigInteger, impactCase: ImpactCase): OffenderManagerOverview? {
    return getOffenderManagerOverview(staffId, teamCode)
      .map { overview ->
        val currentCaseImpact = getCurrentCasePoints(teamCode, overview.code, impactCase.crn)
        overview.potentialCapacity = capacityCalculator.calculate(
          overview.totalPoints.minus(currentCaseImpact)
            .plus(caseCalculator.getPointsForCase(getPotentialCase(crn = impactCase.crn))),
          overview.availablePoints
        )
        overview
      }.block()
  }

  private fun getPotentialCase(crn: String): Case {
    return caseDetailsRepository.findById(crn)
      .map { Case(tier = it.tier, type = it.type, crn = crn) }
      .get()
  }

  private fun getCurrentCasePoints(teamCode: String, staffCode: String, crn: String): BigInteger = offenderManagerRepository.findCaseByTeamCodeAndStaffCodeAndCrn(teamCode, staffCode, crn)?.let { currentCase ->
    return caseCalculator.getPointsForCase(Case(Tier.valueOf(currentCase.tier), CaseType.valueOf(currentCase.caseCategory), false, crn))
  } ?: BigInteger.ZERO

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
        val team = staff.teams?.first { team -> team.code == teamCode }
        getDefaultOffenderManagerOverview(
          staff.staff.forenames,
          staff.staff.surname,
          gradeMapper.deliusToStaffGrade(staff.staffGrade?.code),
          staff.staffCode,
          team!!.description
        )
      }
      overview.potentialCapacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
      overview
    }

  fun getDefaultOffenderManagerOverview(forename: String, surname: String, grade: String, staffCode: String, teamName: String): OffenderManagerOverview {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val defaultAvailablePointsForGrade = getDefaultPointsAvailable(workloadPoints, grade)
    val defaultContractedHours = getDefaultContractedHours(workloadPoints, grade)
    val availablePoints = capacityCalculator.calculateAvailablePoints(
      defaultAvailablePointsForGrade, defaultContractedHours,
      BigDecimal.ZERO, defaultContractedHours
    )
    val overview = OffenderManagerOverview(forename, surname, grade, BigDecimal.ZERO, BigDecimal.ZERO, availablePoints.toBigInteger(), BigInteger.ZERO, staffCode, teamName, BigDecimal.ZERO, defaultContractedHours, LocalDateTime.now(), -1, BigInteger.ZERO)
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

    offenderManagerRepository.findCasesByTeamCodeAndStaffCode(teamCode, offenderManagerCode).let { cases ->
      val crns = cases.map { case -> case.crn }
      val dueEndDate = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(28L)
      sentenceRepository.findByCrnInAndExpectedEndDateGreaterThanEqualAndTerminatedDateIsNull(crns, ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)).let { sentences ->
        val casesDueToEnd = sentences
          .groupBy { sentence -> sentence.crn }
          .mapValues { sentence -> sentence.value.maxOf { it.expectedEndDate } }
          .filter { sentence -> sentence.value.isEqual(dueEndDate) || !sentence.value.isAfter(dueEndDate) }
          .count()
        it.caseEndDue = casesDueToEnd.toBigInteger()
      }
      sentenceRepository.findByCrnInAndExpectedReleaseDateGreaterThanEqualAndTerminatedDateIsNull(crns, ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)).let { sentences ->
        val releasesDueToEnd = sentences
          .groupBy { sentence -> sentence.crn }
          .mapValues { sentence -> sentence.value.maxOf { it.expectedReleaseDate ?: ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault()) } }
          .filter { sentence -> sentence.value.isEqual(dueEndDate) || !sentence.value.isAfter(dueEndDate) }
          .count()
        it.releasesDue = releasesDueToEnd.toBigInteger()
      }
    }

    return it
  } ?: run {
    communityApiClient.getStaffByCode(offenderManagerCode)
      .map { staff ->
        val team = staff.teams?.first { team -> team.code == teamCode }
        getDefaultOffenderManagerOverview(
          staff.staff.forenames,
          staff.staff.surname,
          gradeMapper.deliusToStaffGrade(staff.staffGrade?.code),
          staff.staffCode,
          team!!.description
        )
      }.block()
  }

  override fun getCases(teamCode: String, offenderManagerCode: String): OffenderManagerCases? =
    offenderManagerRepository.findCasesByTeamCodeAndStaffCode(teamCode, offenderManagerCode).let { cases ->
      Mono.zip(
        communityApiClient.getStaffByCode(offenderManagerCode),
        getCrnToOffenderDetails(cases.map { it.crn })
      ).map { results ->
        val team = results.t1.teams?.first { team -> team.code == teamCode }
        OffenderManagerCases.from(results.t1, gradeMapper.deliusToStaffGrade(results.t1.staffGrade?.code), team!!, cases, results.t2)
      }.block()
    }

  private fun getCrnToOffenderDetails(crns: List<String>): Mono<Map<String, OffenderDetails>> {
    return if (crns.isEmpty()) Mono.just(emptyMap()) else offenderSearchApiClient.getOffendersByCrns(crns)
      .map { offenderDetails ->
        offenderDetails.map { offenderDetail ->
          {
            offenderDetail.otherIds.crn to offenderDetail
          }
        }.associate { it.invoke() }
      }
  }
}
