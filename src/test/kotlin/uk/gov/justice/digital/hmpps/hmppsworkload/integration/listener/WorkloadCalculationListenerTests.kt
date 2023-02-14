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
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.WorkforceAllocationsToDeliusExtension.Companion.workforceAllocationsToDelius
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.AdditionalInformation
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.WorkloadCalculationEvent
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

class WorkloadCalculationListenerTests : IntegrationTestBase() {

  @Test
  fun `calculate workload when staff available hours have changed`() {
    val crn = "J678910"
    val staffCode = "staff1"
    val teamCode = "team1"
    val availableHours = BigDecimal.TEN
    val caseDetailsEntity = CaseDetailsEntity(crn, Tier.C3, CaseType.COMMUNITY, "Jane", "Doe")

    caseDetailsRepository.save(caseDetailsEntity)

    workforceAllocationsToDelius.officerViewResponse(staffCode)
    communityApi.staffCodeResponse(staffCode, teamCode)
    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = BigInteger.ONE, staffCode = staffCode, teamCode = teamCode, createdBy = "createdby", providerCode = "providerCode", isActive = true))

    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(staffAvailableHoursChangedEvent(staffCode, teamCode, availableHours))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("staff.available.hours.changed"))
      )
    )

    noMessagesOnWorkloadCalculationEventsQueue()

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
  fun `move event onto dlq when retrieving grade fails`() {
    val crn = "J678910"
    val staffCode = "staff1"
    val teamCode = "team1"
    val availableHours = BigDecimal.TEN
    val caseDetailsEntity = CaseDetailsEntity(crn, Tier.C3, CaseType.COMMUNITY, "Jane", "Doe")

    caseDetailsRepository.save(caseDetailsEntity)

    workforceAllocationsToDelius.officerViewErrorResponse(staffCode)
    personManagerRepository.save(PersonManagerEntity(crn = crn, staffId = BigInteger.ONE, staffCode = staffCode, teamCode = teamCode, createdBy = "createdby", providerCode = "providerCode", isActive = true))

    hmppsDomainSnsClient.publish(
      PublishRequest(hmppsDomainTopicArn, jsonString(staffAvailableHoursChangedEvent(staffCode, teamCode, availableHours))).withMessageAttributes(
        mapOf("eventType" to MessageAttributeValue().withDataType("String").withStringValue("staff.available.hours.changed"))
      )
    )

    noMessagesOnWorkloadCalculationEventsQueue()

    oneMessageOnWorkloadCalculationDeadLetterQueue()
  }

  private fun staffAvailableHoursChangedEvent(staffCode: String, teamCode: String, availableHours: BigDecimal) = WorkloadCalculationEvent(
    AdditionalInformation(availableHours), PersonReference(listOf(PersonReferenceType("staffCode", staffCode), PersonReferenceType("teamCode", teamCode)))
  )
}
