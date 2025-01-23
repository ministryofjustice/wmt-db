package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import java.math.BigInteger

@Service
class JpaBasedCaseCalculator(private val workloadPointsRepository: WorkloadPointsRepository) : CaseCalculator {

  override fun getPointsForCase(potentialCase: Case): BigInteger = workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(potentialCase.isT2A).let {
    val tierPoints = it.getTierPointsMap(potentialCase.type)
    tierPoints[potentialCase.tier]!!
  }
}
