package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.hamcrest.core.IsNot
import org.hamcrest.text.MatchesPattern
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.request.allocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.math.BigInteger

class AllocateCaseToOffenderManager : IntegrationTestBase() {

  @Test
  fun `can allocate CRN to Offender`() {
    val staffId = 123456789L
    val crn = "CRN1"
    val staffCode = "OM1"
    val teamCode = "T1"
    val eventId = BigInteger.valueOf(123456789L)
    staffIdResponse(staffId, staffCode, teamCode)
    offenderSummaryResponse(crn)
    singleActiveRequirementResponse(crn, eventId)
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

    expectPersonAndEventAllocationCompleteMessage(crn)
  }

  @Test
  fun `do not allocate active unpaid requirements`() {
    val staffId = 123456789L
    val crn = "CRN1"
    val staffCode = "OM1"
    val teamCode = "T1"
    val eventId = BigInteger.valueOf(123456789L)
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
    val staffId = BigInteger.valueOf(123456789L)
    val crn = "CRN1"
    val staffCode = "OM1"
    val teamCode = "T1"
    val eventId = BigInteger.valueOf(123456789L)
    val requirementId = BigInteger.valueOf(567891234L)
    staffIdResponse(staffId.longValueExact(), staffCode, teamCode)
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
    val staffId = BigInteger.valueOf(123456789L)
    val crn = "CRN1"
    val staffCode = "OM1"
    val teamCode = "T1"
    val eventId = BigInteger.valueOf(123456789L)
    staffIdResponse(staffId.longValueExact(), staffCode, teamCode)
    offenderSummaryResponse(crn)
    singleActiveUnpaidRequirementResponse(crn, eventId)
    val storedPersonManager = PersonManagerEntity(crn = crn, staffId = BigInteger.ONE, staffCode = "ADIFFERENTCODE", teamCode = teamCode, offenderName = "John Doe", createdBy = "USER1", providerCode = "PV1")
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
  }
}
