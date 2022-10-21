package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import com.amazonaws.services.sqs.model.SendMessageRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.WMTStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.ReductionReasonEntity

class ExtractPlacedEventListenerTestTest : IntegrationTestBase() {

  lateinit var wmtStaff: WMTStaff
  lateinit var reductionReason: ReductionReasonEntity

  @BeforeEach
  fun setupReductionTestData() {
    val reductionCategory = reductionCategoryRepository.save(ReductionCategoryEntity())
    reductionReason = reductionReasonRepository.save(ReductionReasonEntity(reductionCategoryEntity = reductionCategory))

    wmtStaff = setupCurrentWmtStaff("STAFF2", "TEAM2")
  }

  @Test
  fun `must listen to extract placed event`() {
    hmppsExtractPlacedClient.sendMessage(SendMessageRequest(hmppsExtractPlacedQueue.queueUrl, "{}"))

    noMessagesOnExtractPlacedQueue()
    noMessagesOnExtractPlacedDLQ()
  }
}
