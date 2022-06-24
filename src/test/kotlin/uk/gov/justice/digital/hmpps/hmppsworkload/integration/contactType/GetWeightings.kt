package uk.gov.justice.digital.hmpps.hmppsworkload.integration.contactType

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.AdjustmentReasonEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetContactTypeWeightings
import java.math.BigInteger

class GetWeightings : IntegrationTestBase() {

  @Autowired
  protected lateinit var getContactTypeWeightings: GetContactTypeWeightings

  @Test
  fun `must map all contact type codes to their weightings`() {
    val adjustmentReason = AdjustmentReasonEntity(typeCode = "ADJUSTMENT_REASON1", points = BigInteger.TEN)
    adjustmentReasonRepository.save(adjustmentReason)

    val results = getContactTypeWeightings.findAll()

    Assertions.assertEquals(1, results.size)

    Assertions.assertEquals(adjustmentReason.points, results[adjustmentReason.typeCode])
  }
}
