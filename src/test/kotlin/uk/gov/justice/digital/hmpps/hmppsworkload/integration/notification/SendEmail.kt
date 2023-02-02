package uk.gov.justice.digital.hmpps.hmppsworkload.integration.notification

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffName
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType.COMMUNITY
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier.B3
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.AssessRisksNeedsApiExtension.Companion.assessRisksNeedsApi
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSummaryResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveInductionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.NotificationService
import java.math.BigInteger
import java.util.UUID

class SendEmail : IntegrationTestBase() {

  @Autowired
  lateinit var notificationService: NotificationService

  @Test
  fun `sends an email when ROSH cannot be retrieved`() {
    val allocatedOfficer = DeliusStaff(staffIdentifier = BigInteger.ONE, staffCode = "STAFF1", staff = StaffName("Staff", "Member"), email = "simulate-delivered@notifications.service.gov.uk")
    val requirements = emptyList<ConvictionRequirement>()
    val crn = "X123456"
    val allocateCase = AllocateCase(crn, BigInteger.valueOf(123456789), sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)
    val allocatingOfficerUsername = "AllocatingOfficer"
    val token = "token"
    communityApi.singleActiveConvictionResponseForAllConvictions(crn)
    communityApi.singleActiveInductionResponse(crn)
    communityApi.staffUserNameResponse(allocatingOfficerUsername)
    assessRisksNeedsApi.riskSummaryErrorResponse(crn)
    assessRisksNeedsApi.riskPredictorResponse(crn)
    communityApi.assessmentCommunityApiResponse(crn)
    communityApi.offenderSummaryResponse(crn)
    caseDetailsRepository.save(CaseDetailsEntity(crn, B3, COMMUNITY, "Jane", "Doe"))
    val emailSendResponse = notificationService.notifyAllocation(
      allocatedOfficer,
      requirements,
      allocateCase,
      allocatingOfficerUsername,
      token
    ).block()
    assessRisksNeedsApi.verifyRiskSummaryCalled(crn, 2)
    assessRisksNeedsApi.verifyRiskPredictorCalled(crn, 1)
    assertEquals(UUID.fromString("d2708c23-d5d2-4455-b26c-7d5d1d5c5733"), emailSendResponse?.first()?.templateId)
  }

  @Test
  fun `sends an email when risk predictor cannot be retrieved`() {
    val allocatedOfficer = DeliusStaff(staffIdentifier = BigInteger.ONE, staffCode = "STAFF1", staff = StaffName("Staff", "Member"), email = "simulate-delivered@notifications.service.gov.uk")
    val requirements = emptyList<ConvictionRequirement>()
    val crn = "X123456"
    val allocateCase = AllocateCase(crn, BigInteger.valueOf(123456789), sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)
    val allocatingOfficerUsername = "AllocatingOfficer"
    val token = "token"
    communityApi.singleActiveConvictionResponseForAllConvictions(crn)
    communityApi.singleActiveInductionResponse(crn)
    communityApi.staffUserNameResponse(allocatingOfficerUsername)
    assessRisksNeedsApi.riskSummaryResponse(crn)
    assessRisksNeedsApi.riskPredictorErrorResponse(crn)
    communityApi.assessmentCommunityApiResponse(crn)
    communityApi.offenderSummaryResponse(crn)
    caseDetailsRepository.save(CaseDetailsEntity(crn, B3, COMMUNITY, "Jane", "Doe"))
    val emailSendResponse = notificationService.notifyAllocation(
      allocatedOfficer,
      requirements,
      allocateCase,
      allocatingOfficerUsername,
      token
    ).block()
    assessRisksNeedsApi.verifyRiskSummaryCalled(crn, 1)
    assessRisksNeedsApi.verifyRiskPredictorCalled(crn, 2)
    assertEquals(UUID.fromString("d2708c23-d5d2-4455-b26c-7d5d1d5c5733"), emailSendResponse?.first()?.templateId)
  }
}
