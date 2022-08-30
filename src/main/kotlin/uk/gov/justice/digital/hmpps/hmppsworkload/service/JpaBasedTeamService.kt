package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.TeamStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.WorkloadCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZoneId

@Service
class JpaBasedTeamService(
  private val teamRepository: TeamRepository,
  private val capacityCalculator: CapacityCalculator,
  private val communityApiClient: CommunityApiClient,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val personManagerRepository: PersonManagerRepository
) {

  fun getTeamOverview(teamCode: String, grades: List<String>?): List<TeamOverview>? = communityApiClient
    .getTeamStaff(teamCode)
    .map { staff ->
      val workloads = teamRepository.findByOverview(teamCode).associateBy { it.code }
      val caseCountAfter = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(7L)
      val caseCounts = personManagerRepository.findByTeamCodeAndCreatedDateGreaterThanEqualLatest(teamCode, caseCountAfter)
        .groupBy { it.staffCode }
        .mapValues { countEntry -> countEntry.value.size }
      staff.map {
        val overview = workloads[it.staffCode] ?: getTeamOverviewForOffenderManagerWithoutWorkload(it)
        overview.staffId = it.staffIdentifier
        overview.casesInLastWeek = caseCounts.getOrDefault(overview.code, 0).toBigInteger()
        overview.grade = it.grade
        overview.capacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
        overview
      }.filter {
        grades == null || grades.contains(it.grade)
      }
    }.block()

  fun getTeamOverviewForOffenderManagerWithoutWorkload(
    teamStaff: TeamStaff
  ): TeamOverview {
    return TeamOverview(
      teamStaff.staff.forenames, teamStaff.staff.surname, BigDecimal.ZERO, BigDecimal.ZERO,
      defaultAvailablePointsForGrade(teamStaff), BigInteger.ZERO, teamStaff.staffCode
    )
  }

  private fun defaultAvailablePointsForGrade(teamStaff: TeamStaff): BigInteger {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    return workloadPoints.getDefaultPointsAvailable(teamStaff.grade).toBigInteger()
  }

  fun getWorkloadCases(teams: List<String>): Flux<WorkloadCase> {
    return Flux.empty()
  }
}
