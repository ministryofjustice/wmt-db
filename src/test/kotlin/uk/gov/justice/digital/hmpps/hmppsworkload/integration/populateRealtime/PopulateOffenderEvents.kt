package uk.gov.justice.digital.hmpps.hmppsworkload.integration.populateRealtime

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType.CUSTODY
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier.A2
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity

class PopulateOffenderEvents : IntegrationTestBase() {

  @Test
  fun `repopulate case details`() {
    val crn = "CRN1"
    val caseDetailsEntity = CaseDetailsEntity(crn, A2, CUSTODY, "", "")
    caseDetailsRepository.save(caseDetailsEntity)

    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    offenderSummaryResponse(crn)
    tierCalculationResponse(crn)

    webTestClient.post()
      .uri("/cases/populate/name")
      .contentType(APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk

    noMessagesOnOffenderEventsQueue()

    val caseDetail = caseDetailsRepository.findAll().first()
    assertEquals(crn, caseDetail.crn)
    assertEquals(CUSTODY, caseDetail.type)
    assertEquals(Tier.B3, caseDetail.tier)
    assertEquals("Jane", caseDetail.firstName)
    assertEquals("Doe", caseDetail.surname)
  }
}
