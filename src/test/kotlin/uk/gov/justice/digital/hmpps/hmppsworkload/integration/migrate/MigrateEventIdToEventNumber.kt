package uk.gov.justice.digital.hmpps.hmppsworkload.integration.migrate

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.CommunityApiExtension.Companion.communityApi
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RequirementManagerEntity
import java.math.BigInteger

class MigrateEventIdToEventNumber : IntegrationTestBase() {

  @Test
  fun `must migrate all event manager entries which have no event number`() {
    val noEventNumberEntry = EventManagerEntity(crn = "CRN1", staffCode = "OM1", teamCode = "T1", createdBy = "USER1", eventId = BigInteger.valueOf(567891234L), isActive = true, eventNumber = null)
    eventManagerRepository.save(noEventNumberEntry)
    communityApi.convictionById(noEventNumberEntry.crn, noEventNumberEntry.eventId)

    webTestClient.get()
      .uri("/admin/migrate-event-identifiers")
      .exchange()
      .expectStatus()
      .isOk

    val entry = eventManagerRepository.findByIdOrNull(noEventNumberEntry.id!!)!!

    Assertions.assertEquals(1, entry.eventNumber)
  }

  @Test
  fun `must migrate all requirement manager entries which have no event number`() {
    val noEventNumberEntry = RequirementManagerEntity(crn = "CRN1", staffCode = "OM1", teamCode = "T1", createdBy = "USER1", eventId = BigInteger.valueOf(567891234L), requirementId = BigInteger.TEN, isActive = true, eventNumber = null)
    requirementManagerRepository.save(noEventNumberEntry)
    communityApi.convictionById(noEventNumberEntry.crn, noEventNumberEntry.eventId)

    webTestClient.get()
      .uri("/admin/migrate-event-identifiers")
      .exchange()
      .expectStatus()
      .isOk

    val entry = requirementManagerRepository.findByIdOrNull(noEventNumberEntry.id!!)!!

    Assertions.assertEquals(1, entry.eventNumber)
  }

  @Test
  fun `must handle not getting the conviction from community api gracefully`() {
    val errorEventManager = EventManagerEntity(crn = "CRN12345", staffCode = "OM1", teamCode = "T1", createdBy = "USER1", eventId = BigInteger.valueOf(8977043L), isActive = true, eventNumber = null)
    eventManagerRepository.save(errorEventManager)

    val noEventNumberEntry = EventManagerEntity(crn = "CRN1", staffCode = "OM1", teamCode = "T1", createdBy = "USER1", eventId = BigInteger.valueOf(567891234L), isActive = true, eventNumber = null)
    eventManagerRepository.save(noEventNumberEntry)
    communityApi.convictionById(noEventNumberEntry.crn, noEventNumberEntry.eventId)

    webTestClient.get()
      .uri("/admin/migrate-event-identifiers")
      .exchange()
      .expectStatus()
      .isOk

    val entry = eventManagerRepository.findByIdOrNull(noEventNumberEntry.id!!)!!

    Assertions.assertEquals(1, entry.eventNumber)
  }
}
