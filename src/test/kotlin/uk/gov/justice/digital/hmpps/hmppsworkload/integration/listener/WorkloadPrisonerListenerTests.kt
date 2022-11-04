package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import com.fasterxml.jackson.databind.node.ObjectNode
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReferenceType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

class WorkloadPrisonerListenerTests : IntegrationTestBase() {

  @Test
  fun `calculate workload for managed prisoner`() {
    val crn = "J678910"
    val nomsNumber = "X1111XX"
    val staffCode = "staff1"
    val teamCode = "team1"
    val availableHours = BigDecimal.valueOf(37)
    val caseDetailsEntity = CaseDetailsEntity(crn, Tier.C3, CaseType.COMMUNITY, "Jane", "Doe")

    caseDetailsRepository.save(caseDetailsEntity)

    staffCodeResponse(staffCode, teamCode)
    nomsLookupRespond(crn, nomsNumber)
    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = BigInteger.ONE, staffCode = staffCode, teamCode = teamCode, offenderName = "offender", createdBy = "createdby", providerCode = "providerCode", isActive = true))

    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(prisonerEvent(nomsNumber))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("prison-offender-events.prisoner.released"))
      )
    )


    noMessagesOnWorkloadPrisonerQueue()

    val actualWorkloadCalcEntity: WorkloadCalculationEntity? =
      workloadCalculationRepository.findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(staffCode, teamCode)

    Assertions.assertAll(
      { Assertions.assertEquals(staffCode, actualWorkloadCalcEntity?.staffCode) },
      { Assertions.assertEquals(teamCode, actualWorkloadCalcEntity?.teamCode) },
      { Assertions.assertEquals(availableHours, actualWorkloadCalcEntity?.breakdownData?.availableHours) },
      { Assertions.assertEquals(LocalDateTime.now().dayOfMonth, actualWorkloadCalcEntity?.calculatedDate?.dayOfMonth) }
    )
  }

  @Test
  fun `process prisoner who is unknown to workload`() {
    val crn = "J678910"
    val nomsNumber = "X1111XX"
    nomsLookupRespond(crn, nomsNumber)
    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(prisonerEvent(nomsNumber))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("prison-offender-events.prisoner.released"))
      )
    )


    noMessagesOnWorkloadPrisonerQueue()
  }

  private fun prisonerEvent(nomsNumber: String) = HmppsMessage<ObjectNode>("prison-offender-events.prisoner.released", 1, "A prisoner has been released from prison", "", "2022-11-04T09:39:14Z", objectMapper.createObjectNode(), PersonReference(
    listOf(PersonReferenceType("NOMS", nomsNumber))
  ))
}