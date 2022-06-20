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
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.CourtAppearance
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Offence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenceDetail
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderAssessment
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RequirementCategory
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Sentence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.SentenceType
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffName
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Team
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.emailResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.DateMapper
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper
import uk.gov.service.notify.NotificationClientApi
import uk.gov.service.notify.SendEmailResponse
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

class EmailNotificationServiceTests {
  private val notificationClient = mockk<NotificationClientApi>()
  private val communityApiClient = mockk<CommunityApiClient>()
  private val hmppsCaseDetailsRepo = mockk<CaseDetailsRepository>()
  private val gradeMapper = mockk<GradeMapper>()
  private val dateMapper = mockk<DateMapper>()
  private val assessRisksNeedsApiClient = mockk<AssessRisksNeedsApiClient>()
  private val templateId = "templateId"
  private val notificationService = EmailNotificationService(
    notificationClient, templateId, communityApiClient,
    gradeMapper, dateMapper, assessRisksNeedsApiClient, hmppsCaseDetailsRepo
  )

  @BeforeEach
  fun setup() {
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(
      listOf(
        Conviction(
          Sentence(
            SentenceType("", ""),
            BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
          ),
          null, true, BigInteger.TEN,
          CourtAppearance(
            LocalDateTime.now(), "Court 1"
          ),
          emptyList()
        )
      )
    )
    every { communityApiClient.getInductionContacts(any(), any()) } returns Mono.just(emptyList())
    every { communityApiClient.getStaffByUsername(any()) } returns Mono.just(Staff(BigInteger.ONE, "ALLOCATOR1", StaffName("Alli", "Cator"), null, null, null, "all1@cat0r.com"))
    every { assessRisksNeedsApiClient.getRiskSummary(any(), any()) } returns Mono.just(Optional.empty())
    every { assessRisksNeedsApiClient.getRiskPredictors(any(), any()) } returns Mono.just(emptyList())
    every { communityApiClient.getAssessment(any()) } returns Mono.just(Optional.empty())
    every { gradeMapper.deliusToStaffGrade(any()) } returns ""
    every { notificationClient.sendEmail(any(), any(), any(), any()) } returns SendEmailResponse(emailResponse())
    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.CUSTODY)
  }

  @Test
  fun `must add case name to email`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token).block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("${personSummary.firstName} ${personSummary.surname}", parameters.captured["case_name"])
  }

  @Test
  fun `must add crn to email`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(allocateCase.crn, parameters.captured["crn"])
  }

  @Test
  fun `must add officer name`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}", parameters.captured["officer_name"])
  }

  @Test
  fun `must add court name`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(activeConviction.courtAppearance!!.courtName, parameters.captured["court_name"])
  }

  @Test
  fun `must add court sentence date`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(activeConviction.courtAppearance!!.appearanceDate.format(DateTimeFormatter.ISO_LOCAL_DATE), parameters.captured["sentence_date"])
  }

  @Test
  fun `must add induction statement no induction appointment needed when case type is custody`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("No induction appointment is needed", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add induction statement booked and due on when initial appointment is booked in the future`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.COMMUNITY)
    val appointment = Contact(ZonedDateTime.now().plusDays(5L))
    every { communityApiClient.getInductionContacts(any(), any()) } returns Mono.just(listOf(appointment))

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Their induction has been booked and is due on ${appointment.contactStart.format(DateTimeFormatter.ISO_LOCAL_DATE)}", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add induction statement is overdue and was due on when initial appointment is booked in the past`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.COMMUNITY)
    val appointment = Contact(ZonedDateTime.now().minusDays(5L))
    every { communityApiClient.getInductionContacts(any(), any()) } returns Mono.just(listOf(appointment))

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Their induction is overdue and was due on ${appointment.contactStart.format(DateTimeFormatter.ISO_LOCAL_DATE)}", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add induction statement has not been booked and is due on when initial appointment is not booked at all`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    every { hmppsCaseDetailsRepo.findByIdOrNull(any()) } returns CaseDetailsEntity("", Tier.B3, CaseType.COMMUNITY)

    every { communityApiClient.getInductionContacts(any(), any()) } returns Mono.just(emptyList())

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    val dueDate = LocalDate.now().plusDays(10L)
    every { dateMapper.addBusinessDays(activeConviction.sentence!!.startDate, any()) } returns dueDate

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Their induction has not been booked and is due on ${dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}", parameters.captured["induction_statement"])
  }

  @Test
  fun `must add offences`() {

    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val offence = Offence(true, OffenceDetail("Offence main category", "", ""))

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      listOf(offence)
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf(offence.detail.mainCategoryDescription), parameters.captured["offences"])
  }

  @Test
  fun `must add order`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val sentence = Sentence(SentenceType("", ""), BigInteger.ONE, "Minutes", "Sentence Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null)

    val activeConviction = Conviction(
      sentence, null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )
    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("${sentence.description} (${sentence.originalLength} ${sentence.originalLengthUnits})", parameters.captured["order"])
  }

  @Test
  fun `must add requirements`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirement = ConvictionRequirement(
      RequirementCategory("MAIN", "Main Category"), RequirementCategory("SUB", "Sub Category"),
      BigInteger.TEN, BigInteger.ONE, "Year"
    )
    val requirements = listOf(requirement)
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf("${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length} ${requirement.lengthUnit}"), parameters.captured["requirements"])
  }

  @Test
  fun `must add tier`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val tier = "B3"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(Tier.B3, parameters.captured["tier"])
  }

  @Test
  fun `must add rosh capitalized when it exists`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val riskSummary = RiskSummary("HIGH")
    every { assessRisksNeedsApiClient.getRiskSummary(allocateCase.crn, token) } returns Mono.just(Optional.of(riskSummary))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("High", parameters.captured["rosh"])
  }

  @Test
  fun `must add RSR level capitalized when it exists`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val riskPredictor = RiskPredictor(BigDecimal.TEN, "MEDIUM", LocalDateTime.now())

    every { assessRisksNeedsApiClient.getRiskPredictors(allocateCase.crn, token) } returns Mono.just(listOf(riskPredictor))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Medium", parameters.captured["rsrLevel"])
  }

  @Test
  fun `must add RSR percentage when it exists`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val riskPredictor = RiskPredictor(BigDecimal.TEN, "MEDIUM", LocalDateTime.now())

    every { assessRisksNeedsApiClient.getRiskPredictors(allocateCase.crn, token) } returns Mono.just(listOf(riskPredictor))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(riskPredictor.rsrPercentageScore.toString(), parameters.captured["rsrPercentage"])
  }

  @Test
  fun `must add ogrs percentage when it exists`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val offenderAssessment = OffenderAssessment(LocalDate.now(), BigInteger.TEN)

    every { communityApiClient.getAssessment(any()) } returns Mono.just(Optional.of(offenderAssessment))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(offenderAssessment.ogrsScore.toString(), parameters.captured["ogrsPercentage"])
  }

  @Test
  fun `must add ogrs level low when its below 49`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val offenderAssessment = OffenderAssessment(LocalDate.now(), BigInteger.TEN)

    every { communityApiClient.getAssessment(any()) } returns Mono.just(Optional.of(offenderAssessment))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Low", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add ogrs level medium when its between 50 and 74`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val offenderAssessment = OffenderAssessment(LocalDate.now(), BigInteger.valueOf(55L))

    every { communityApiClient.getAssessment(any()) } returns Mono.just(Optional.of(offenderAssessment))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Medium", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add ogrs level high when its between 75 and 89`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val offenderAssessment = OffenderAssessment(LocalDate.now(), BigInteger.valueOf(80L))

    every { communityApiClient.getAssessment(any()) } returns Mono.just(Optional.of(offenderAssessment))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("High", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add ogrs level very high when its 90 or more`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val offenderAssessment = OffenderAssessment(LocalDate.now(), BigInteger.valueOf(95L))

    every { communityApiClient.getAssessment(any()) } returns Mono.just(Optional.of(offenderAssessment))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("Very High", parameters.captured["ogrsLevel"])
  }

  @Test
  fun `must add previous convictions when it exists`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )

    val previousOffence = Offence(true, OffenceDetail("Previous main", "Previous sub", "Previous Description"))

    val previousConvictions = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, false, BigInteger.TWO,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      listOf(previousOffence)
    )

    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction, previousConvictions))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf(previousOffence.detail.description), parameters.captured["previousConvictions"])
  }

  @Test
  fun `must provide default previous convictions when only active convictions exist`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN)
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val activeConviction = Conviction(
      Sentence(
        SentenceType("", ""),
        BigInteger.ONE, "Minutes", "Description", LocalDate.now(), BigInteger.ONE, LocalDate.now(), null
      ),
      null, true, BigInteger.TEN,
      CourtAppearance(
        LocalDateTime.now(), "Court 1"
      ),
      emptyList()
    )

    every { communityApiClient.getAllConvictions(any()) } returns Mono.just(listOf(activeConviction))

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(listOf("N/A"), parameters.captured["previousConvictions"])
  }

  @Test
  fun `must add notes when they exist`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN, "Some Notes")
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(allocateCase.instructions, parameters.captured["notes"])
  }

  @Test
  fun `must add allocating officer name`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN, "Some Notes")
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val allocatingOfficer = Staff(BigInteger.ONE, "ALLOCATOR1", StaffName("Alli", "Cator"), null, null, null, "all1@cat0r.com")

    every { communityApiClient.getStaffByUsername(any()) } returns Mono.just(allocatingOfficer)

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals("${allocatingOfficer.staff.forenames} ${allocatingOfficer.staff.surname}", parameters.captured["allocatingOfficerName"])
  }

  @Test
  fun `must add allocating officer grade`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN, "Some Notes")
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val mappedGrade = "ALLOCATING OFFICER GRADE"
    every { gradeMapper.deliusToStaffGrade(any()) } returns mappedGrade

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(mappedGrade, parameters.captured["allocatingOfficerGrade"])
  }

  @Test
  fun `must add allocating officer team name`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN, "Some Notes")
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    val team = Team(teamCode, "Team Description")
    val allocatingOfficer = Staff(BigInteger.ONE, "ALLOCATOR1", StaffName("Alli", "Cator"), null, listOf(team), null, "all1@cat0r.com")

    every { communityApiClient.getStaffByUsername(any()) } returns Mono.just(allocatingOfficer)

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token)
      .block()
    val parameters = slot<MutableMap<String, Any>>()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, capture(parameters), isNull()) }
    Assertions.assertEquals(team.description, parameters.captured["allocatingOfficerTeam"])
  }

  @Test
  fun `must email all addresses supplied`() {
    val personSummary = PersonSummary("John", "Doe")
    val allocatedOfficer = Staff(BigInteger.ONE, "STFFCDE1", StaffName("Sally", "Socks"), null, null, null, "email1@email.com")
    val requirements = emptyList<ConvictionRequirement>()
    val firstEmail = "first@email.com"
    val secondEmail = "second@email.com"
    val allocateCase = AllocateCase("CRN1111", BigInteger.TEN, null, listOf(firstEmail, secondEmail))
    val allocatingOfficerUsername = "ALLOCATOR"
    val teamCode = "TM1"
    val token = "token"

    notificationService.notifyAllocation(allocatedOfficer, personSummary, requirements, allocateCase, allocatingOfficerUsername, teamCode, token).block()
    verify(exactly = 1) { notificationClient.sendEmail(templateId, allocatedOfficer.email!!, any(), isNull()) }
    verify(exactly = 1) { notificationClient.sendEmail(templateId, firstEmail, any(), isNull()) }
    verify(exactly = 1) { notificationClient.sendEmail(templateId, secondEmail, any(), isNull()) }
  }
}
