package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReferenceType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.TierApiExtension.Companion.hmppsTier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.WorkforceAllocationsToDeliusExtension
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.TierCalculationEventListener.CalculationEventData

class TierCalculationEventListenerTest : IntegrationTestBase() {

  @Test
  fun `saves updated tiers`() {
    val crn = "J678910"
    communityApi.singleActiveConvictionResponse(crn)
    WorkforceAllocationsToDeliusExtension.workforceAllocationsToDelius.personResourceResponse("J678910", "Jane", "hi, hi", "Doe", CaseType.CUSTODY)
    hmppsTier.tierCalculationResponse(crn)
    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, objectMapper.writeValueAsString(tierCalculationEvent(crn)))
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

  private fun tierCalculationEvent(crn: String) = CalculationEventData(
    PersonReference(listOf(PersonReferenceType("CRN", crn)))
  )
}
