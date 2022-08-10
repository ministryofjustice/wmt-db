package uk.gov.justice.digital.hmpps.hmppsworkload.integration.notification

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffName
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType.COMMUNITY
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier.B3
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.EmailNotificationService
import java.math.BigInteger
import java.util.UUID

class SendEmail : IntegrationTestBase() {

  @Autowired
  lateinit var emailNotificationService: EmailNotificationService
  @Test
  fun `sends an email when ROSH cannot be retrieved`() {
    val allocatedOfficer = Staff(staffIdentifier = BigInteger.ONE, staffCode = "STAFF1", staff = StaffName("Staff", "Member"), email = "simulate-delivered@notifications.service.gov.uk")
    val personSummary = PersonSummary("firstname", "surname")
    val requirements = emptyList<ConvictionRequirement>()
    val crn = "X123456"
    val allocateCase = AllocateCase(crn, BigInteger.valueOf(123456789))
    val allocatingOfficerUsername = "AllocatingOfficer"
    val token = "token"
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveInductionResponse(crn)
    staffUserNameResponse(allocatingOfficerUsername)
    riskSummaryErrorResponse(crn)
    riskPredictorResponse(crn)
    assessmentCommunityApiResponse(crn)
    caseDetailsRepository.save(CaseDetailsEntity(crn, B3, COMMUNITY))
    val emailSendResponse = emailNotificationService.notifyAllocation(
      allocatedOfficer,
      personSummary,
      requirements,
      allocateCase,
      allocatingOfficerUsername,
      token
    ).block()
    assertEquals(UUID.fromString("d2708c23-d5d2-4455-b26c-7d5d1d5c5733"), emailSendResponse?.first()?.templateId)
  }
}
