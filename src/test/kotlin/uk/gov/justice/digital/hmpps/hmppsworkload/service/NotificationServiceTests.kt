package uk.gov.justice.digital.hmpps.hmppsworkload.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.AssessRisksNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenceDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Requirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskOGRS
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.getAllocationDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.emailResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.service.notify.NotificationClientApi
import uk.gov.service.notify.SendEmailResponse
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

class NotificationServiceTests {
  private val notificationClient = mockk<NotificationClientApi>()
  private val hmppsCaseDetailsRepo = mockk<CaseDetailsRepository>()
  private val assessRisksNeedsApiClient = mockk<AssessRisksNeedsApiClient>()
  private val templateId = "templateId"
  private val notificationService = NotificationService(
    notificationClient, templateId,
    assessRisksNeedsApiClient, hmppsCaseDetailsRepo
  )
  private val allocateCase = AllocateCase("CRN1111", sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)
  private val personSummary = PersonSummary("John", "Doe")

  @BeforeEach
  fun setup() {

    every { assessRisksNeedsApiClient.getRiskSummary(any(), any()) } returns Mono.just(Optional.empty())
    every { assessRisksNeedsApiClient.getRiskPredictors(any(), any()) } returns Mono.just(emptyList())
    every { notificationClient.sendEmail(any(), any(), any(), any()) } returns SendEmailResponse(emailResponse())
    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.CUSTODY, "Jane", "Doe")
  }

  @Test
  fun `must add case name to email`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token).block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("${allocationDetails.name.getCombinedName()}", parameters.captured["case_name"])
  }

  @Test
  fun `must add crn to email`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(allocateCase.crn, parameters.captured["crn"])
  }

  @Test
  fun `must add officer name`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("${allocationDetails.staff.name.getCombinedName()}", parameters.captured["officer_name"])
  }

  @Test
  fun `must add court name`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(allocationDetails.court.name, parameters.captured["court_name"])
  }

  @Test
  fun `must add court sentence date`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(allocationDetails.court.appearanceDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")), parameters.captured["sentence_date"])
  }

  @Test
  fun `must add induction statement no induction appointment needed when case type is custody`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("no initial appointment needed", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add induction statement booked and due on when initial appointment is booked in the future`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn, LocalDate.now().plusDays(5L))
    val token = "token"

    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.COMMUNITY, "Jane", "Doe")

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("their initial appointment is scheduled for ${allocationDetails.initialAppointment!!.date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add induction statement is overdue and was due on when initial appointment is booked in the past`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn, LocalDate.now().minusDays(5L))
    val token = "token"

    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.COMMUNITY, "Jane", "Doe")

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("their initial appointment was scheduled for ${allocationDetails.initialAppointment!!.date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))}", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add induction statement has not been booked and is due on when initial appointment is not booked at all`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val token = "token"

    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.COMMUNITY, "Jane", "Doe")

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("no date found for the initial appointment, please check with your team", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add offences`() {

    val offenceDetails = OffenceDetails("Offence main category")

    val allocationDetails = getAllocationDetails(allocateCase.crn, offenceDetails = listOf(offenceDetails))

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf(offenceDetails.mainCategory), parameters.captured["offences"])
  }

  @Test
  fun `must add order`() {

    val allocationDetails = getAllocationDetails(allocateCase.crn)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("${allocationDetails.sentence.description} (${allocationDetails.sentence.length})", parameters.captured["order"])
  }

  @Test
  fun `must add requirements`() {
    val requirement = Requirement(
      "Main Category", "Sub Category",
      "1 Year", BigInteger.ONE
    )
    val allocationDetails = getAllocationDetails(allocateCase.crn, activeRequirements = listOf(requirement))
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf("${requirement.mainCategory}: ${requirement.subCategory} ${requirement.length}"), parameters.captured["requirements"])
  }

  @Test
  fun `must add requirements without length`() {
    val requirement = Requirement(
      "Main Category", "Sub Category",
      "", BigInteger.ONE
    )
    val allocationDetails = getAllocationDetails(allocateCase.crn, activeRequirements = listOf(requirement))
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf("${requirement.mainCategory}: ${requirement.subCategory}"), parameters.captured["requirements"])
  }

  @Test
  fun `must add rosh capitalized when it exists`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val token = "token"

    val riskSummary = RiskSummary("HIGH")
    every { assessRisksNeedsApiClient.getRiskSummary(allocateCase.crn, token) } returns Mono.just(Optional.of(riskSummary))

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("High", parameters.captured["rosh"])
  }

  @Test
  fun `must add RSR level capitalized when it exists`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val token = "token"

    val riskPredictor = RiskPredictor(BigDecimal.TEN, "MEDIUM", LocalDateTime.now())

    every { assessRisksNeedsApiClient.getRiskPredictors(allocateCase.crn, token) } returns Mono.just(listOf(riskPredictor))

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("Medium", parameters.captured["rsrLevel"])
  }

  @Test
  fun `must add RSR percentage when it exists`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val token = "token"

    val riskPredictor = RiskPredictor(BigDecimal.TEN, "MEDIUM", LocalDateTime.now())

    every { assessRisksNeedsApiClient.getRiskPredictors(allocateCase.crn, token) } returns Mono.just(listOf(riskPredictor))

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(riskPredictor.rsrPercentageScore.toString(), parameters.captured["rsrPercentage"])
  }

  @Test
  fun `must add ogrs percentage when it exists`() {

    val ogrs = RiskOGRS(LocalDate.now(), 10)
    val allocationDetails = getAllocationDetails(allocateCase.crn, ogrs = ogrs)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(ogrs.score.toString(), parameters.captured["ogrsPercentage"])
  }

  @Test
  fun `must add ogrs level low when its below 49`() {
    val ogrs = RiskOGRS(LocalDate.now(), 10)
    val allocationDetails = getAllocationDetails(allocateCase.crn, ogrs = ogrs)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("Low", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add ogrs level medium when its between 50 and 74`() {
    val ogrs = RiskOGRS(LocalDate.now(), 55)
    val allocationDetails = getAllocationDetails(allocateCase.crn, ogrs = ogrs)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("Medium", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add ogrs level high when its between 75 and 89`() {
    val ogrs = RiskOGRS(LocalDate.now(), 80)
    val allocationDetails = getAllocationDetails(allocateCase.crn, ogrs = ogrs)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("High", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add ogrs level very high when its 90 or more`() {
    val ogrs = RiskOGRS(LocalDate.now(), 95)
    val allocationDetails = getAllocationDetails(allocateCase.crn, ogrs = ogrs)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("Very High", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add notes when they exist`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val allocateCase = AllocateCase("CRN1111", "Some Notes", sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)

    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(allocateCase.instructions, parameters.captured["notes"])
  }

  @Test
  fun `must add allocating officer name`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val allocateCase = AllocateCase("CRN1111", "Some Notes", sendEmailCopyToAllocatingOfficer = false, eventNumber = 1)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals("${allocationDetails.allocatingStaff.name.getCombinedName()}", parameters.captured["allocatingOfficerName"])
  }

  @Test
  fun `must add allocating officer grade`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, capture(parameters), isNull()) }
    Assertions.assertEquals(allocationDetails.allocatingStaff.getGrade(), parameters.captured["allocatingOfficerGrade"])
  }

  @Test
  fun `must email all addresses supplied`() {
    val allocationDetails = getAllocationDetails(allocateCase.crn)
    val firstEmail = "first@email.com"
    val secondEmail = "second@email.com"
    val allocateCase = AllocateCase("CRN1111", "instructions", listOf(firstEmail, secondEmail), false, 1)
    val token = "token"

    notificationService.notifyAllocation(allocationDetails, allocateCase, token).block()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocationDetails.staff.email, any(), isNull()) }
    verify(exactly = 1) { notificationClient.sendEmail(templateId, firstEmail, any(), isNull()) }
    verify(exactly = 1) { notificationClient.sendEmail(templateId, secondEmail, any(), isNull()) }
  }
}
