package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.MissingTeamError
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Optional

@Service
class JpaBasedTeamService(
  private val teamRepository: TeamRepository,
  private val capacityCalculator: CapacityCalculator,
  private val communityApiClient: CommunityApiClient,
  private val gradeMapper: GradeMapper,
  private val workloadPointsRepository: WorkloadPointsRepository
) : TeamService {

  override fun getTeamOverview(teamCode: String, grades: List<String>?): List<TeamOverview>? {
    return communityApiClient
      .getTeamStaff(teamCode)
      .map { staff ->
        val overviews = teamRepository.findByOverview(teamCode).associateBy {
          it.capacity = capacityCalculator.calculate(it.totalPoints, it.availablePoints)
          it.code
        }
        Optional.of(
          staff.map {
            overviews[it.staffCode]?.let { teamOverview ->
              teamOverview.staffId = it.staffIdentifier
              teamOverview.grade = gradeMapper.workloadToStaffGrade(teamOverview.grade)
              teamOverview
            } ?: run {
              getTeamOverviewForOffenderManagerWithoutWorkload(it.staff.forenames, it.staff.surname, gradeMapper.deliusToStaffGrade(it.staffGrade?.code), it.staffCode, it.staffIdentifier)
            }
          }.filter {
            grades == null || grades.contains(it.grade)
          }
        )
      }
      .onErrorResume { ex ->
        when (ex) {
          is MissingTeamError -> Mono.just(Optional.empty())
          else -> Mono.error(ex)
        }
      }.block()?.orElse(null)
  }

  fun getTeamOverviewForOffenderManagerWithoutWorkload(forename: String, surname: String, grade: String, staffCode: String, staffId: BigInteger): TeamOverview {
    val workloadPoints = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
    val defaultAvailablePoints = getDefaultPointsAvailable(workloadPoints, grade)
    val overview = TeamOverview(forename, surname, grade, BigDecimal.ZERO, BigDecimal.ZERO, defaultAvailablePoints.toBigInteger(), BigInteger.ZERO, staffCode)
    overview.capacity = capacityCalculator.calculate(overview.totalPoints, overview.availablePoints)
    overview.staffId = staffId
    return overview
  }

  fun getDefaultPointsAvailable(workloadPoints: WorkloadPointsEntity, grade: String): BigDecimal {
    return when (grade) {
      "SPO" -> workloadPoints.defaultAvailablePointsSPO
      else -> workloadPoints.defaultAvailablePointsPO
    }
  }
}
