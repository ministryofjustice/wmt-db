package uk.gov.justice.digital.hmpps.hmppsworkload.integration.notification

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType.COMMUNITY
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier.B3
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.getAllocationDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.AssessRisksNeedsApiExtension.Companion.assessRisksNeedsApi
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.NotificationService
import java.util.UUID

class SendEmail : IntegrationTestBase() {

  @Autowired
  lateinit var notificationService: NotificationService

  @Test
  fun `sends an email when ROSH cannot be retrieved`() {
    val crn = "X123456"
    val allocateCase = AllocateCase(crn, sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)
    val token = "token"
    val allocationDetails = getAllocationDetails(crn)

    assessRisksNeedsApi.riskSummaryErrorResponse(crn)
    assessRisksNeedsApi.riskPredictorResponse(crn)
    caseDetailsRepository.save(CaseDetailsEntity(crn, B3, COMMUNITY, "Jane", "Doe"))
    val emailSendResponse = notificationService.notifyAllocation(
      allocationDetails,
      allocateCase,
      token
    ).block()
    assessRisksNeedsApi.verifyRiskSummaryCalled(crn, 2)
    assessRisksNeedsApi.verifyRiskPredictorCalled(crn, 1)
    assertEquals(UUID.fromString("d2708c23-d5d2-4455-b26c-7d5d1d5c5733"), emailSendResponse?.first()?.templateId)
  }

  @Test
  fun `sends an email when risk predictor cannot be retrieved`() {

    val crn = "X123456"
    val allocateCase = AllocateCase(crn, sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)
    val token = "token"
    val allocationDetails = getAllocationDetails(crn)
    assessRisksNeedsApi.riskSummaryResponse(crn)
    assessRisksNeedsApi.riskPredictorErrorResponse(crn)
    caseDetailsRepository.save(CaseDetailsEntity(crn, B3, COMMUNITY, "Jane", "Doe"))
    val emailSendResponse = notificationService.notifyAllocation(
      allocationDetails,
      allocateCase,
      token
    ).block()
    assessRisksNeedsApi.verifyRiskSummaryCalled(crn, 1)
    assessRisksNeedsApi.verifyRiskPredictorCalled(crn, 2)
    assertEquals(UUID.fromString("d2708c23-d5d2-4455-b26c-7d5d1d5c5733"), emailSendResponse?.first()?.templateId)
  }
}
