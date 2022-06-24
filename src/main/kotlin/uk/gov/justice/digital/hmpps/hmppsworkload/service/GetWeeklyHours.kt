package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTWorkloadOwnerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import java.math.BigDecimal

@Service
class GetWeeklyHours(private val wmtWorkloadOwnerRepository: WMTWorkloadOwnerRepository, private val workloadPointsRepository: WorkloadPointsRepository) {

  fun findWeeklyHours(teamCode: String, staffCode: String, staffGrade: String): BigDecimal = wmtWorkloadOwnerRepository.findByTeamCodeAndOffenderManagerCode(teamCode, staffCode)?.contractedHours
    ?: getDefaultWeeklyHoursForGrade(staffGrade)

  private fun getDefaultWeeklyHoursForGrade(staffGrade: String): BigDecimal = getDefaultContractedHours(workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false), staffGrade)

  private fun getDefaultContractedHours(workloadPoints: WorkloadPointsEntity, grade: String): BigDecimal {
    return when (grade) {
      "PO", "PQiP" -> workloadPoints.defaultContractedHoursPO
      "PSO" -> workloadPoints.defaultContractedHoursPSO
      else -> workloadPoints.defaultContractedHoursSPO
    }
  }
}
