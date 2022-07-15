package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import com.microsoft.applicationinsights.TelemetryClient
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.core.IsNot
import org.hamcrest.text.MatchesPattern
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.request.allocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.emailResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.NotificationService
import uk.gov.justice.digital.hmpps.hmppsworkload.service.TelemetryEventType.PERSON_MANAGER_ALLOCATED
import uk.gov.justice.digital.hmpps.hmppsworkload.service.getWmtPeriod
import uk.gov.service.notify.SendEmailResponse
import java.math.BigInteger
import java.time.LocalDateTime

class AllocateCaseToOffenderManager : IntegrationTestBase() {

  @MockkBean
  private lateinit var notificationService: NotificationService
  @MockkBean
  private lateinit var telemetryClient: TelemetryClient

  private val crn = "CRN1"
  private val staffId = BigInteger.valueOf(123456789L)

  private val staffCode = "OM1"
  private val teamCode = "T1"
  private val eventId = BigInteger.valueOf(123456789L)
  @BeforeEach
  fun setupApiCalls() {
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
    every { notificationService.notifyAllocation(any(), any(), any(), any(), any(), teamCode, any()) } returns Mono.just(
      listOf(
        SendEmailResponse(
          emailResponse()
        )
      )
    )

    every { telemetryClient.trackEvent(any(), any(), null) } returns Unit
  }

  @Test
  fun `can allocate CRN to Offender`() {
    staffIdResponse(staffId, staffCode, teamCode)
    offenderSummaryResponse(crn)
    singleActiveRequirementResponse(crn, eventId)

    caseDetailsRepository.save(CaseDetailsEntity(crn, Tier.A0, CaseType.CUSTODY))

    webTestClient.post()
      .uri("/team/$teamCode/offenderManagers/$staffId/cases")
      .bodyValue(allocateCase(crn, eventId))
      .headers {
        it.authToken(roles = listOf("ROLE_MANAGE_A_WORKFORCE_ALLOCATE"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.personManagerId")
      .value(MatchesPattern.matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
      .jsonPath("$.eventManagerId")
      .value(MatchesPattern.matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
      .jsonPath("$.requirementManagerIds[0]")
      .value(MatchesPattern.matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))

    expectWorkloadAllocationCompleteMessages(crn)

    val actualWorkloadCalcEntity: WorkloadCalculationEntity =
      workloadCalculationRepository.findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(staffCode, teamCode)!!

    Assertions.assertAll(
      { Assertions.assertEquals(staffCode, actualWorkloadCalcEntity.staffCode) },
      { Assertions.assertEquals(teamCode, actualWorkloadCalcEntity.teamCode) },
      { Assertions.assertEquals(LocalDateTime.now().dayOfMonth, actualWorkloadCalcEntity.calculatedDate.dayOfMonth) },
      { Assertions.assertEquals(1, actualWorkloadCalcEntity.breakdownData.caseloadCount) }
    )

    verify(exactly = 1) { notificationService.notifyAllocation(any(), any(), any(), any(), any(), teamCode, any()) }
    verify(exactly = 1) {
      telemetryClient.trackEvent(
        PERSON_MANAGER_ALLOCATED.eventName,
        mapOf(
          "crn" to crn,
          "teamCode" to teamCode,
          "providerCode" to "N01",
          "staffId" to "123456789",
          "wmtPeriod" to getWmtPeriod(LocalDateTime.now())
        ),
        null
      )
    }
  }

  @Test
  fun `do not allocate active unpaid requirements`() {
    staffIdResponse(staffId, staffCode, teamCode)
    offenderSummaryResponse(crn)
    singleActiveUnpaidRequirementResponse(crn, eventId)

    webTestClient.post()
      .uri("/team/$teamCode/offenderManagers/$staffId/cases")
      .bodyValue(allocateCase(crn, eventId))
      .headers {
        it.authToken(roles = listOf("ROLE_MANAGE_A_WORKFORCE_ALLOCATE"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.requirementManagerIds")
      .isEmpty
  }

  @Test
  fun `can allocate an already managed CRN to same staff member`() {
    val requirementId = BigInteger.valueOf(567891234L)
    staffIdResponse(staffId, staffCode, teamCode)
    staffIdResponse(staffId, staffCode, teamCode)
    offenderSummaryResponse(crn)
    singleActiveRequirementResponse(crn, eventId, requirementId)
    val storedPersonManager = PersonManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, offenderName = "John Doe", createdBy = "USER1", providerCode = "PV1")
    personManagerRepository.save(storedPersonManager)
    val storedEventManager = EventManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, eventId = eventId, createdBy = "USER1", providerCode = "PV1")
    eventManagerRepository.save(storedEventManager)
    val storedRequirementManager = RequirementManagerEntity(crn = crn, staffId = staffId, staffCode = staffCode, teamCode = teamCode, eventId = eventId, requirementId = requirementId, createdBy = "USER1", providerCode = "PV1")
    requirementManagerRepository.save(storedRequirementManager)

    webTestClient.post()
      .uri("/team/$teamCode/offenderManagers/$staffId/cases")
      .bodyValue(allocateCase(crn, eventId))
      .headers {
        it.authToken(roles = listOf("ROLE_MANAGE_A_WORKFORCE_ALLOCATE"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.personManagerId")
      .isEqualTo(storedPersonManager.uuid.toString())
      .jsonPath("$.eventManagerId")
      .isEqualTo(storedEventManager.uuid.toString())
      .jsonPath("$.requirementManagerIds[0]")
      .isEqualTo(storedRequirementManager.uuid.toString())
  }

  @Test
  fun `can allocate an already managed CRN to different staff member`() {
    staffIdResponse(staffId, staffCode, teamCode)
    offenderSummaryResponse(crn)
    singleActiveUnpaidRequirementResponse(crn, eventId)
    val storedPersonManager = PersonManagerEntity(crn = crn, staffId = BigInteger.ONE, staffCode = "ADIFFERENTCODE", teamCode = "TEAMCODE", offenderName = "John Doe", createdBy = "USER1", providerCode = "PV1")
    staffIdResponse(storedPersonManager.staffId, storedPersonManager.staffCode, storedPersonManager.teamCode)
    personManagerRepository.save(storedPersonManager)

    webTestClient.post()
      .uri("/team/$teamCode/offenderManagers/$staffId/cases")
      .bodyValue(allocateCase(crn, eventId))
      .headers {
        it.authToken(roles = listOf("ROLE_MANAGE_A_WORKFORCE_ALLOCATE"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.personManagerId")
      .value(MatchesPattern.matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
      .jsonPath("$.personManagerId")
      .value(IsNot.not(storedPersonManager.uuid.toString()))

    val actualWorkloadCalcEntity: WorkloadCalculationEntity? =
      workloadCalculationRepository.findFirstByStaffCodeAndTeamCodeOrderByCalculatedDate(storedPersonManager.staffCode, storedPersonManager.teamCode)

    Assertions.assertAll(
      { Assertions.assertEquals(storedPersonManager.staffCode, actualWorkloadCalcEntity!!.staffCode) },
      { Assertions.assertEquals(storedPersonManager.teamCode, actualWorkloadCalcEntity!!.teamCode) },
      { Assertions.assertEquals(LocalDateTime.now().dayOfMonth, actualWorkloadCalcEntity!!.calculatedDate.dayOfMonth) }
    )
  }
}
