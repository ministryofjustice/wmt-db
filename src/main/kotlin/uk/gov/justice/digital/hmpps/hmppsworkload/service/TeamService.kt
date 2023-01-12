package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerWorkload
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Practitioner
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PractitionerWorkload
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
class TeamService(
  private val teamRepository: TeamRepository,
  private val communityApiClient: CommunityApiClient,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val personManagerRepository: PersonManagerRepository,
  private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient
) {
  fun getPractitioner(teamCodes: List<String>, crn: String, grades: List<String>?): PractitionerWorkload {
    return workforceAllocationsToDeliusApiClient.getPractitioner(crn, teamCodes)
      .map { choosePractitionerResponse ->

        val workloads =
          teamRepository.findAllByTeamCodes(teamCodes).associateBy { team -> team.code }
        val caseCountAfter = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(7L)

        val caseCounts = personManagerRepository.findByTeamCodeInAndCreatedDateGreaterThanEqualAndIsActiveIsTrue(teamCodes, caseCountAfter)
          .groupBy { it.staffCode }
          .mapValues { countEntry -> countEntry.value.size }

        PractitionerWorkload(
          choosePractitionerResponse.crn,
          choosePractitionerResponse.name,
          choosePractitionerResponse.probationStatus,
          choosePractitionerResponse.communityPersonManager,
          choosePractitionerResponse.teams.mapValues {
            it.value
              .filter {
                grades == null || grades.contains(it.grade)
              }
              .map {
                val overview = workloads[it.code] ?: getTeamOverviewForOffenderManagerWithoutWorkload(
                  it.code,
                  it.grade ?: "DMY"
                )
                Practitioner(
                  it.code,
                  it.name,
                  it.email.takeUnless { it.isEmpty() },
                  it.grade ?: "DMY",
                  calculateCapacity(overview.totalPoints, overview.availablePoints),
                  caseCounts.getOrDefault(it.code, 0),
                  overview.totalCommunityCases,
                  overview.totalCustodyCases
                )
              }
          }
        )
      }.block()
  }

  fun getWorkloadCases(teams: List<String>): Flux<WorkloadCase> {
    return Flux.fromIterable(teamRepository.findWorkloadCountCaseByCode(teams))
      .map { WorkloadCase(it.teamCode, it.totalCases, calculateCapacity(it.totalPoints.toBigInteger(), it.availablePoints.toBigInteger()).toDouble()) }
  }

  fun getTeamOverview(teamCode: String, grades: List<String>?): List<OffenderManagerWorkload>? = communityApiClient
    .getTeamStaff(teamCode)
    .map { staff ->
      val workloads = teamRepository.findByOverview(teamCode).associateBy { it.code }
      val caseCountAfter = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(7L)
      val caseCounts = personManagerRepository.findByTeamCodeAndCreatedDateGreaterThanEqualAndIsActiveIsTrue(teamCode, caseCountAfter)
        .groupBy { it.staffCode }
        .mapValues { countEntry -> countEntry.value.size }
      staff.map {
        val overview = workloads[it.staffCode] ?: getTeamOverviewForOffenderManagerWithoutWorkload(
          it.staffCode,
          it.grade
        )
        OffenderManagerWorkload(
          it.staff.forenames, it.staff.surname, it.email, it.grade, overview.totalCommunityCases,
          overview.totalCustodyCases, calculateCapacity(overview.totalPoints, overview.availablePoints), it.staffCode, caseCounts.getOrDefault(overview.code, 0).toBigInteger()
        )
      }.filter {
        grades == null || grades.contains(it.grade)
      }
    }.block()

  private fun getTeamOverviewForOffenderManagerWithoutWorkload(
    staffCode: String,
    grade: String
  ): TeamOverview {
    return TeamOverview(
      BigDecimal.ZERO, BigDecimal.ZERO,
      defaultAvailablePointsForGrade(grade), BigInteger.ZERO, staffCode
    )
  }

  private fun defaultAvailablePointsForGrade(grade: String): BigInteger {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    return workloadPoints.getDefaultPointsAvailable(grade).toBigInteger()
  }
}
