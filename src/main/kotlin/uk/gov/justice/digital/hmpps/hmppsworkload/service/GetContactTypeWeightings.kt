package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.AdjustmentReasonRepository
import java.math.BigInteger

@Service
class GetContactTypeWeightings(private val adjustmentReasonRepository: AdjustmentReasonRepository) {

  fun findAll(): Map<String, BigInteger> = adjustmentReasonRepository.findAll()
    .associateBy({ it.typeCode }, { it.points })
}
