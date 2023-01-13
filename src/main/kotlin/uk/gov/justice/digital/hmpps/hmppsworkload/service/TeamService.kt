package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OffenderManagerWorkload
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Practitioner
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PractitionerWorkload
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.WorkloadCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZoneId

@Service
class TeamService(
  private val teamRepository: TeamRepository,
  private val communityApiClient: CommunityApiClient,
  private val workloadPointsRepository: WorkloadPointsRepository,
  private val personManagerRepository: PersonManagerRepository,
  private val caseDetailsRepository: CaseDetailsRepository,
  private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient
) {
  fun getPractitioners(teamCodes: List<String>, crn: String, grades: List<String>?): PractitionerWorkload? {
    return workforceAllocationsToDeliusApiClient.choosePractitioners(crn, teamCodes)
      .map { choosePractitionerResponse ->
        val practitionerWorkloads = teamRepository.findAllByTeamCodes(teamCodes).associateBy { teamStaffId(it.teamCode, it.staffCode) }
        val caseCountAfter = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(7L)
        val practitionerCaseCounts = personManagerRepository.findByTeamCodeInAndCreatedDateGreaterThanEqualAndIsActiveIsTrue(teamCodes, caseCountAfter)
          .groupBy { teamStaffId(it.teamCode, it.staffCode) }
          .mapValues { countEntry -> countEntry.value.size }

        val enrichedTeams = choosePractitionerResponse.teams.mapValues { team ->
          team.value
            .filter { grades == null || grades.contains(it.grade) }
            .map {
              val teamStaffId = teamStaffId(team.key, it.code)
              val practitionerWorkload = practitionerWorkloads[teamStaffId] ?: getTeamOverviewForOffenderManagerWithoutWorkload(it.code, it.grade ?: "DMY", team.key)
              Practitioner.from(it, practitionerWorkload, practitionerCaseCounts.getOrDefault(teamStaffId, 0))
            }
        }
        PractitionerWorkload.from(
          choosePractitionerResponse,
          caseDetailsRepository.findByIdOrNull(crn)!!.tier,
          enrichedTeams
        )
      }.block()
  }

  private fun teamStaffId(teamCode: String, staffCode: String) = "$teamCode-$staffCode"

  fun getWorkloadCases(teams: List<String>): Flux<WorkloadCase> {
    return Flux.fromIterable(teamRepository.findWorkloadCountCaseByCode(teams))
      .map { WorkloadCase(it.teamCode, it.totalCases, calculateCapacity(it.totalPoints.toBigInteger(), it.availablePoints.toBigInteger()).toDouble()) }
  }

  fun getTeamOverview(teamCode: String, grades: List<String>?): List<OffenderManagerWorkload>? = communityApiClient
    .getTeamStaff(teamCode)
    .map { staff ->
      val workloads = teamRepository.findByOverview(teamCode).associateBy { it.staffCode }
      val caseCountAfter = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(7L)
      val caseCounts = personManagerRepository.findByTeamCodeAndCreatedDateGreaterThanEqualAndIsActiveIsTrue(teamCode, caseCountAfter)
        .groupBy { it.staffCode }
        .mapValues { countEntry -> countEntry.value.size }
      staff.map {
        val overview = workloads[it.staffCode] ?: getTeamOverviewForOffenderManagerWithoutWorkload(
          it.staffCode,
          it.grade,
          teamCode
        )
        OffenderManagerWorkload(
          it.staff.forenames, it.staff.surname, it.email, it.grade, overview.totalCommunityCases,
          overview.totalCustodyCases, calculateCapacity(overview.totalPoints, overview.availablePoints), it.staffCode, caseCounts.getOrDefault(overview.staffCode, 0).toBigInteger()
        )
      }.filter {
        grades == null || grades.contains(it.grade)
      }
    }.block()

  private fun getTeamOverviewForOffenderManagerWithoutWorkload(
    staffCode: String,
    grade: String,
    teamCode: String
  ): TeamOverview {
    return TeamOverview(
      0, 0,
      defaultAvailablePointsForGrade(grade), BigInteger.ZERO, staffCode, teamCode
    )
  }

  private fun defaultAvailablePointsForGrade(grade: String): BigInteger {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    return workloadPoints.getDefaultPointsAvailable(grade).toBigInteger()
  }
}
