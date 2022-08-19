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
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
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

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }
    await untilCallTo { countMessagesOnOffenderEventDeadLetterQueue() } matches { it == 0 }

    val caseCount = caseDetailsRepository.count()

    Assertions.assertEquals(0, caseCount)
  }

  @Test
  fun `do not write case details if there is no tier`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationNotFoundResponse(crn)
    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }
    await untilCallTo { countMessagesOnOffenderEventDeadLetterQueue() } matches { it == 0 }

    val caseCount = caseDetailsRepository.count()

    Assertions.assertEquals(0, caseCount)
  }

  @Test
  fun `must save case details when processing new sentence event`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    val staffCode = "staff1"
    val teamCode = "team1"
    val staffId = BigInteger.ONE
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    staffCodeResponse(staffCode, teamCode)
    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, offenderName = "offender", createdBy = "createdby", providerCode = "providerCode", isActive = true))

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
    val staffCode = "staff1"
    val teamCode = "team1"
    val staffId = BigInteger.ONE
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn, Tier.C3.name)

    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, offenderName = "offender", createdBy = "createdby", providerCode = "providerCode", isActive = true))

    staffCodeResponse(staffCode, teamCode)
    staffCodeResponse(staffCode, teamCode)
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

    val personManagerEntity = personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = BigInteger.ONE, staffCode = "STFFCDE", teamCode = "TM1", offenderName = "Name", createdBy = "USER1", providerCode = "PV1", isActive = true))
    val eventManagerEntity = eventManagerRepository.save(EventManagerEntity(crn = crn, eventId = BigInteger.TEN, staffId = BigInteger.ONE, staffCode = "STFFCDE", teamCode = "TM1", createdBy = "USER1", providerCode = "PV1", isActive = true))
    val requirementManagerEntity = requirementManagerRepository.save(RequirementManagerEntity(crn = crn, eventId = BigInteger.TEN, requirementId = BigInteger.TWO, staffId = BigInteger.ONE, staffCode = "STFFCDE", teamCode = "TM1", createdBy = "USER1", providerCode = "PV1", isActive = true))

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
    Assertions.assertFalse(personManagerRepository.findByIdOrNull(personManagerEntity.id!!)!!.isActive)
    Assertions.assertFalse(eventManagerRepository.findByIdOrNull(eventManagerEntity.id!!)!!.isActive)
    Assertions.assertFalse(requirementManagerRepository.findByIdOrNull(requirementManagerEntity.id!!)!!.isActive)
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
  fun `do not save sentence when terminated`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleInactiveConvictionsResponse(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    Assertions.assertEquals(0, countMessagesOnOffenderEventDeadLetterQueue())

    Assertions.assertNull(sentenceRepository.findBySentenceId(sentenceId))
  }

  @Test
  fun `delete sentence if there is a termination date`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleInactiveConvictionsResponse(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
    val savedSentence = SentenceEntity(
      sentenceId, crn, LocalDate.of(2019, 11, 17).atStartOfDay(ZoneId.systemDefault()), LocalDate.of(2020, 5, 16).atStartOfDay(ZoneId.systemDefault()), "SC",
      LocalDate.of(2020, 6, 23).atStartOfDay(ZoneId.systemDefault())
    )
    sentenceRepository.save(savedSentence)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    Assertions.assertEquals(0, countMessagesOnOffenderEventDeadLetterQueue())

    Assertions.assertNull(sentenceRepository.findBySentenceId(sentenceId))
  }

  @Test
  fun `do not update sentence if it has not changed`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
    val savedSentence = SentenceEntity(
      sentenceId, crn, LocalDate.of(2019, 11, 17).atStartOfDay(ZoneId.systemDefault()), LocalDate.of(2020, 5, 16).atStartOfDay(ZoneId.systemDefault()), "SC",
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

  @Test
  fun `update case details when there is a change`() {
    val crn = "J678910"
    val sentenceId = BigInteger.valueOf(2500278160L)
    val staffCode = "staff1"
    val teamCode = "team1"
    val staffId = BigInteger.ONE
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    val caseDetailsEntity = CaseDetailsEntity(crn, Tier.C3, CaseType.COMMUNITY)
    staffCodeResponse(staffCode, teamCode)
    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, offenderName = "offender", createdBy = "createdby", providerCode = "providerCode", isActive = true))
    caseDetailsRepository.save(caseDetailsEntity)

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    assertThat(caseDetailsRepository.count()).isEqualTo(1)
    assertThat(caseDetailsRepository.findByIdOrNull(crn)?.tier).isEqualTo(Tier.B3)
  }

  @Test
  fun `calculate workload when updating case details for realtime offender manager`() {
    val crn = "J678910"
    val staffCode = "staff1"
    val teamCode = "team1"
    val staffId = BigInteger.ONE

    val sentenceId = BigInteger.valueOf(2500278160L)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)

    val caseDetailsEntity = CaseDetailsEntity(crn, Tier.C3, CaseType.COMMUNITY)

    caseDetailsRepository.save(caseDetailsEntity)

    staffCodeResponse(staffCode, teamCode)
    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, offenderName = "offender", createdBy = "createdby", providerCode = "providerCode", isActive = true))

    hmppsOffenderSnsClient.publish(
      PublishRequest(hmppsOffenderTopicArn, jsonString(offenderEvent(crn, sentenceId))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("SENTENCE_CHANGED"))
      )
    )

    await untilCallTo { countMessagesOnOffenderEventQueue() } matches { it == 0 }

    val actualWorkloadCalcEntity: WorkloadCalculationEntity? =
      workloadCalculationRepository.findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(staffCode, teamCode)

    Assertions.assertAll(
      { Assertions.assertEquals(staffCode, actualWorkloadCalcEntity?.staffCode) },
      { Assertions.assertEquals(teamCode, actualWorkloadCalcEntity?.teamCode) },
      { Assertions.assertEquals(LocalDateTime.now().dayOfMonth, actualWorkloadCalcEntity?.calculatedDate?.dayOfMonth) }
    )
  }
}
