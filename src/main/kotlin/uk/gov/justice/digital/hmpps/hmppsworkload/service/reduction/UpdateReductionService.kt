package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.OutOfDateReductions
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import javax.transaction.Transactional

@Service
class UpdateReductionService(private val reductionsRepository: ReductionsRepository) {

  @Transactional
  fun updateReductionStatus(outOfDateReductions: OutOfDateReductions) {
    outOfDateReductions.activeNowArchived
      .onEach { it.status = ReductionStatus.ARCHIVED }
      .let { reductionsRepository.saveAll(it) }

    outOfDateReductions.scheduledNowActive
      .onEach { it.status = ReductionStatus.ACTIVE }
      .let { reductionsRepository.saveAll(it) }
  }
}
