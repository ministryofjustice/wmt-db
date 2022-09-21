package uk.gov.justice.digital.hmpps.hmppsworkload.integration.populateRealtime

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity

class PopulateOffenderEvents : IntegrationTestBase() {

  @Test
  fun `repopulate case details`() {
    val crn = "CRN1"
    val caseDetailsEntity = CaseDetailsEntity(crn, Tier.A2, CaseType.CUSTODY, "", "")
    caseDetailsRepository.save(caseDetailsEntity)

    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    offenderSummaryResponse(crn)
    tierCalculationResponse(crn)

    webTestClient.post()
      .uri("/cases/populate/name")
      .contentType(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk

    noMessagesOnOffenderEventsQueue()

    val caseDetail = caseDetailsRepository.findAll().first()
    Assertions.assertEquals(crn, caseDetail.crn)
    Assertions.assertEquals(CaseType.CUSTODY, caseDetail.type)
    Assertions.assertEquals(Tier.B3, caseDetail.tier)
    Assertions.assertEquals("Jane", caseDetail.firstName)
    Assertions.assertEquals("Doe", caseDetail.surname)
  }
}
