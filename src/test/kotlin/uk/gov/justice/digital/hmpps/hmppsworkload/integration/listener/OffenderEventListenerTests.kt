package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZoneId

class OffenderEventListenerTests : IntegrationTestBase() {

  @Test
  fun `must save case details when processing new sentence event`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo {
      caseDetailsRepository.count()
    } matches { it!! > 0 }

    val caseDetail = caseDetailsRepository.findAll().first()

    Assertions.assertEquals(crn, caseDetail.crn)
    Assertions.assertEquals(CaseType.CUSTODY, caseDetail.type)
    Assertions.assertEquals(Tier.B3, caseDetail.tier)
    Assertions.assertNotNull(caseDetail.createdDate)
  }

  @Test
  fun `must save case details only when new sentence event`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )
    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    val count = caseDetailsRepository.count()

    Assertions.assertEquals(1, count)
  }

  @Test
  fun `must save sentence when processing new sentence event`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { sentenceRepository.count() } matches { it!! > 0 }

    val sentence = sentenceRepository.findAll().first()

    Assertions.assertEquals(sentenceId, sentence.sentenceId)
    Assertions.assertEquals(crn, sentence.crn)
    Assertions.assertEquals(LocalDate.of(2019, 11, 17).atStartOfDay(ZoneId.systemDefault()), sentence.startDate)
    Assertions.assertEquals(LocalDate.of(2020, 5, 16).atStartOfDay(ZoneId.systemDefault()), sentence.expectedEndDate)
    Assertions.assertEquals("SC", sentence.sentenceTypeCode)
    Assertions.assertEquals(LocalDate.of(2020, 6, 23).atStartOfDay(ZoneId.systemDefault()), sentence.expectedReleaseDate)
  }

  @Test
  fun `must only save one instance of sentence if multiple events come through`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    val savedSentence = SentenceEntity(
      null, sentenceId, crn, LocalDate.of(2019, 11, 17).atStartOfDay(ZoneId.systemDefault()), LocalDate.of(2020, 5, 16).atStartOfDay(ZoneId.systemDefault()), null, "SC",
      LocalDate.of(2020, 6, 23).atStartOfDay(ZoneId.systemDefault())
    )
    sentenceRepository.save(savedSentence)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    assertThat(sentenceRepository.count()).isEqualTo(1)
  }
}
