package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReference
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.PersonReferenceType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.TierApiExtension.Companion.hmppsTier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.WorkforceAllocationsToDeliusExtension.Companion.workforceAllocationsToDelius
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.WorkloadPrisonerEvent
import java.math.BigDecimal
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
    workforceAllocationsToDelius.officerViewResponse(staffCode)
    workforceAllocationsToDelius.personResponseByNoms(nomsNumber, crn, CaseType.COMMUNITY)
    hmppsTier.tierCalculationResponse(crn)

    personManagerRepository.save(PersonManagerEntity(crn = crn, staffCode = staffCode, teamCode = teamCode, createdBy = "createdby", isActive = true))

    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(prisonerEvent(nomsNumber))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("prison-offender-events.prisoner.released")),
      ),
    )

    noMessagesOnWorkloadPrisonerQueue()
    noMessagesOnWorkloadPrisonerDLQ()

    val actualWorkloadCalcEntity: WorkloadCalculationEntity? =
      workloadCalculationRepository.findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(staffCode, teamCode)

    Assertions.assertAll(
      { Assertions.assertEquals(staffCode, actualWorkloadCalcEntity?.staffCode) },
      { Assertions.assertEquals(teamCode, actualWorkloadCalcEntity?.teamCode) },
      { Assertions.assertEquals(availableHours, actualWorkloadCalcEntity?.breakdownData?.availableHours) },
      { Assertions.assertEquals(LocalDateTime.now().dayOfMonth, actualWorkloadCalcEntity?.calculatedDate?.dayOfMonth) },
    )
  }

  @Test
  fun `process prisoner who is unknown to workload`() {
    val crn = "J678910"
    val nomsNumber = "X1111XX"
    workforceAllocationsToDelius.personResponseByNoms(nomsNumber, crn)
    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(prisonerEvent(nomsNumber))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("prison-offender-events.prisoner.released")),
      ),
    )

    noMessagesOnWorkloadPrisonerQueue()
    noMessagesOnWorkloadPrisonerDLQ()
  }

  @Test
  fun `process prisoner not in Delius yet`() {
    val nomsNumber = "X1111XX"
    workforceAllocationsToDelius.notFoundPersonResourceResponse(nomsNumber, "NOMS")
    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(prisonerEvent(nomsNumber))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("prison-offender-events.prisoner.released")),
      ),
    )

    noMessagesOnWorkloadPrisonerQueue()
    noMessagesOnWorkloadPrisonerDLQ()
  }

  private fun prisonerEvent(nomsNumber: String) = WorkloadPrisonerEvent(
    PersonReference(
      listOf(PersonReferenceType("NOMS", nomsNumber)),
    ),
  )
}
