package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.AdjustmentReasonRepository

@Service
class GetContactTypeWeightings(private val adjustmentReasonRepository: AdjustmentReasonRepository) {

  fun findAll(): Map<String, Int> = adjustmentReasonRepository.findAll()
    .associateBy({ it.typeCode }, { it.points })
}
