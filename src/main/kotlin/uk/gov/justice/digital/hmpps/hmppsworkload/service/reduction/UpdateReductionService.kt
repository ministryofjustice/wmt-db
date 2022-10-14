package uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class UpdateReductionService(private val reductionsRepository: ReductionsRepository) {

  @Transactional
  fun updateReductionStatus() {
    reductionsRepository.findAllByEffectiveFromBeforeAndEffectiveToBeforeAndStatus(
      ZonedDateTime.now(),
      ZonedDateTime.now(),
      ReductionStatus.ACTIVE
    )
      .takeIf { it.isNotEmpty() }?.onEach { it.status = ReductionStatus.ARCHIVED }
      ?.let { reductionsRepository.saveAll(it) }

    reductionsRepository.findAllByEffectiveFromBeforeAndEffectiveToAfterAndStatus(
      ZonedDateTime.now(),
      ZonedDateTime.now(),
      ReductionStatus.SCHEDULED
    )
      .takeIf { it.isNotEmpty() }?.onEach { it.status = ReductionStatus.ACTIVE }
      ?.let { reductionsRepository.saveAll(it) }
  }
}
