package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
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
) : TeamService {

  override fun getTeamOverview(teamCode: String, grades: List<String>?): List<TeamOverview>? = communityApiClient
    .getTeamStaff(teamCode)
    .map { staff ->
      val overviews = teamRepository.findByOverview(teamCode).associateBy {
        it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
        it.code
      }
      val caseCountAfter = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(7L)
      val caseCounts = personManagerRepository.findByTeamCodeAndCreatedDateGreaterThanEqualLatest(teamCode, caseCountAfter)
        .groupBy { it.staffCode }
        .mapValues { countEntry -> countEntry.value.size }
      staff.map {
        val overview = overviews[it.staffCode]?.let { teamOverview ->
          teamOverview.staffId = it.staffIdentifier
          teamOverview.casesInLastWeek = caseCounts.getOrDefault(teamOverview.code, 0).toBigInteger()
          teamOverview
        } ?: getTeamOverviewForOffenderManagerWithoutWorkload(it.staff.forenames, it.staff.surname, it.grade, it.staffCode, it.staffIdentifier)
        overview.grade = it.grade
        overview
      }.filter {
        grades == null || grades.contains(it.grade)
      }
    }.block()

  fun getTeamOverviewForOffenderManagerWithoutWorkload(forename: String, surname: String, grade: String, staffCode: String, staffId: BigInteger): TeamOverview {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val overview = TeamOverview(forename, surname, BigDecimal.ZERO, BigDecimal.ZERO, workloadPoints.getDefaultPointsAvailable(grade).toBigInteger(), BigInteger.ZERO, staffCode)
    overview.capacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
    overview.staffId = staffId
    return overview
  }
}
