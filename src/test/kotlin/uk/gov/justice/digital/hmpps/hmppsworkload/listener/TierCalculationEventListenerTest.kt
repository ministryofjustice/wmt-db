package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

class TierCalculationEventListenerTest : IntegrationTestBase() {

  @Test
  fun `saves updated tiers`() {
    val crn = "J678910"
    singleActiveConvictionResponse(crn)
    offenderSummaryResponse(crn)
    tierCalculationResponse(crn)
    val calcEvent = "{\"crn\":\"J678910\",\"calculationId\":\"e45559d1-3460-4a0e-8281-c736de57c562\"}"
    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, calcEvent)
        .withMessageAttributes(
          mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("TIER_CALCULATION_COMPLETE"))
        )
    )

    await untilCallTo {
      caseDetailsRepository.count()
    } matches { it!! > 0 }

    val caseDetail = caseDetailsRepository.findAll().first()

    Assertions.assertEquals(crn, caseDetail.crn)
    Assertions.assertEquals(CaseType.CUSTODY, caseDetail.type)
    Assertions.assertEquals(Tier.B3, caseDetail.tier)
  }
}
