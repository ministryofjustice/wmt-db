package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZoneId

class OffenderEventListenerTests : IntegrationTestBase() {

  @Test
  fun `case type is unknown if there is no sentence`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    convictionWithNoSentenceResponse(crn)
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
    Assertions.assertEquals(CaseType.UNKNOWN, caseDetail.type)
    Assertions.assertEquals(Tier.B3, caseDetail.tier)
  }

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
  }

  @Test
  fun `only save latest case details if they have changed`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn, Tier.C3.name)

    val sentenceChangedEvent =
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )

    hmppsOffenderSnsClient.publish(sentenceChangedEvent)
    hmppsOffenderSnsClient.publish(sentenceChangedEvent)

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    val count = caseDetailsRepository.count()

    Assertions.assertEquals(1, count)

    val caseDetail = caseDetailsRepository.findByIdOrNull(crn)!!
    Assertions.assertEquals(Tier.C3, caseDetail.tier)
  }

  @Test
  fun `case details not saved if no active convictions exist`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleInactiveConvictionsResponse(crn)
    noConvictionsResponse(crn)

    val sentenceChangedEvent =
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )

    hmppsOffenderSnsClient.publish(sentenceChangedEvent)

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    Assertions.assertEquals(0, countMessagesOnOffenderEventDeadLetterQueue())
    val count = caseDetailsRepository.count()

    Assertions.assertEquals(0, count)
  }

  @Test
  fun `case details deleted if no active convictions exist`() {
    val crn = "J678910"
    caseDetailsRepository.save(CaseDetailsEntity(crn, Tier.C1, CaseType.COMMUNITY))

    val sentenceId = BigInteger.valueOf(2500278160L)
    singleInactiveConvictionsResponse(crn)
    noConvictionsResponse(crn)

    val sentenceChangedEvent =
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )

    hmppsOffenderSnsClient.publish(sentenceChangedEvent)

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    Assertions.assertEquals(0, countMessagesOnOffenderEventDeadLetterQueue())
    val count = caseDetailsRepository.count()

    Assertions.assertEquals(0, count)
  }

  @Test
  fun `must save sentence when processing new sentence event`() {
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
  fun `do not update sentence if it has not changed`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
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
