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
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.TierApiExtension.Companion.hmppsTier

class OffenderDetailsEventListenerTests : IntegrationTestBase() {

  @Test
  fun `must save case details when processing new sentence event`() {
    val crn = "J678910"

    communityApi.singleActiveConvictionResponse(crn)
    communityApi.offenderSummaryResponse(crn)
    hmppsTier.tierCalculationResponse(crn)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("OFFENDER_DETAILS_CHANGED"))
      )
    )

    await untilCallTo {
      caseDetailsRepository.count()
    } matches { it!! > 0 }

    val caseDetail = caseDetailsRepository.findAll().first()

    Assertions.assertEquals(crn, caseDetail.crn)
    Assertions.assertEquals(CaseType.CUSTODY, caseDetail.type)
    Assertions.assertEquals(Tier.B3, caseDetail.tier)
    Assertions.assertEquals("Jane", caseDetail.firstName)
    Assertions.assertEquals("Doe", caseDetail.surname)
  }

  @Test
  fun `must save forbidden offender summary as restricted access`() {
    val crn = "J678910"

    communityApi.singleActiveConvictionResponse(crn)
    communityApi.forbiddenOffenderSummaryResponse(crn)
    hmppsTier.tierCalculationResponse(crn)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("OFFENDER_DETAILS_CHANGED"))
      )
    )

    await untilCallTo {
      caseDetailsRepository.count()
    } matches { it!! > 0 }

    val caseDetail = caseDetailsRepository.findAll().first()

    Assertions.assertEquals("Restricted", caseDetail.firstName)
    Assertions.assertEquals("Access", caseDetail.surname)
  }
}
