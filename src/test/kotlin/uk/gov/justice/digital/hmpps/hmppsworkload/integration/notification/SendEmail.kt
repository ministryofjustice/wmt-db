package uk.gov.justice.digital.hmpps.hmppsworkload.integration.notification

import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import reactor.util.context.Context
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
  fun `sends an email when ROSH cannot be retrieved`() = runBlocking(Context.of(HttpHeaders.AUTHORIZATION, "token").asCoroutineContext()) {
    val crn = "X123456"
    val allocateCase = AllocateCase(crn, sendEmailCopyToAllocatingOfficer = false, eventNumber = 1, allocationJustificationNotes = "some notes", sensitiveNotes = false)
    val allocationDetails = getAllocationDetails(crn)
    val caseDetailsEntity = CaseDetailsEntity(crn, B3, COMMUNITY, "Jane", "Doe")

    assessRisksNeedsApi.riskSummaryErrorResponse(crn)
    assessRisksNeedsApi.riskPredictorResponse(crn)
    caseDetailsRepository.save(caseDetailsEntity)
    val emailSendResponse = notificationService.notifyAllocation(
      allocationDetails,
      allocateCase,
      caseDetailsEntity,
    )
    assessRisksNeedsApi.verifyRiskSummaryCalled(crn, 2)
    assessRisksNeedsApi.verifyRiskPredictorCalled(crn, 1)
    assertEquals(UUID.fromString("5db23c80-9cb6-4b8e-a0f6-56061e50a9ef"), emailSendResponse.first().templateId)
  }

  @Test
  fun `sends an email when risk predictor cannot be retrieved`() = runBlocking(Context.of(HttpHeaders.AUTHORIZATION, "token").asCoroutineContext()) {
    val crn = "X123456"
    val allocateCase = AllocateCase(crn, sendEmailCopyToAllocatingOfficer = false, eventNumber = 1, allocationJustificationNotes = "some notes", sensitiveNotes = false)
    val allocationDetails = getAllocationDetails(crn)
    val caseDetailsEntity = CaseDetailsEntity(crn, B3, COMMUNITY, "Jane", "Doe")

    assessRisksNeedsApi.riskSummaryResponse(crn)
    assessRisksNeedsApi.riskPredictorErrorResponse(crn)
    caseDetailsRepository.save(caseDetailsEntity)
    val emailSendResponse = notificationService.notifyAllocation(
      allocationDetails,
      allocateCase,
      caseDetailsEntity,
    )
    assessRisksNeedsApi.verifyRiskSummaryCalled(crn, 1)
    assessRisksNeedsApi.verifyRiskPredictorCalled(crn, 2)
    assertEquals(UUID.fromString("5db23c80-9cb6-4b8e-a0f6-56061e50a9ef"), emailSendResponse.first().templateId)
  }
}
