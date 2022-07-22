package uk.gov.justice.digital.hmpps.hmppsworkload.integration.offenderManager

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.request.impactCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity

class GetImpactForOffenderManager : IntegrationTestBase() {

  @Test
  fun `can get impact for an offender manager with workload`() {
    val crn = "CRN1"
    val staffCode = "OM1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode)
    caseDetailsRepository.save(CaseDetailsEntity(crn, Tier.B3, CaseType.CUSTODY))

    webTestClient.post()
      .uri("/team/$teamCode/offenderManager/$staffCode/impact")
      .bodyValue(impactCase(crn))
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Ben")
      .jsonPath("$.surname")
      .isEqualTo("Doe")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.capacity")
      .isEqualTo(50)
      .jsonPath("$.code")
      .isEqualTo(staffCode)
      .jsonPath("$.potentialCapacity")
      .isEqualTo(55)
  }

  @Test
  fun `must not change capacity if case already allocated to the officer and is classified the same`() {

    val crn = "CRN2222"
    val staffCode = "OM1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode)
    caseDetailsRepository.save(CaseDetailsEntity(crn, Tier.B3, CaseType.CUSTODY))
    webTestClient.post()
      .uri("/team/$teamCode/offenderManager/$staffCode/impact")
      .bodyValue(impactCase(crn))
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Ben")
      .jsonPath("$.surname")
      .isEqualTo("Doe")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.capacity")
      .isEqualTo(50)
      .jsonPath("$.code")
      .isEqualTo(staffCode)
      .jsonPath("$.potentialCapacity")
      .isEqualTo(50)
  }

  @Test
  fun `can get impact for an offender manager without workload`() {

    val crn = "CRN1"
    val staffCode = "NOWORKLOAD1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode)
    caseDetailsRepository.save(CaseDetailsEntity(crn, Tier.B3, CaseType.CUSTODY))
    webTestClient.post()
      .uri("/team/$teamCode/offenderManager/$staffCode/impact")
      .bodyValue(impactCase(crn))
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.grade")
      .isEqualTo("PO")
      .jsonPath("$.capacity")
      .isEqualTo(0)
      .jsonPath("$.code")
      .isEqualTo(staffCode)
      .jsonPath("$.potentialCapacity")
      .isEqualTo(2)
  }

  @Test
  fun `can get impact for an offender manager without a grade and a workload`() {
    val crn = "CRN1"
    val staffCode = "NOWORKLOAD1"
    val teamCode = "T1"
    staffCodeResponse(staffCode, teamCode, "UNKNOWNGRADECODE")
    caseDetailsRepository.save(CaseDetailsEntity(crn, Tier.B3, CaseType.CUSTODY))
    webTestClient.post()
      .uri("/team/$teamCode/offenderManager/$staffCode/impact")
      .bodyValue(impactCase(crn))
      .headers {
        it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT"))
        it.contentType = MediaType.APPLICATION_JSON
      }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.forename")
      .isEqualTo("Sheila")
      .jsonPath("$.surname")
      .isEqualTo("Hancock")
      .jsonPath("$.grade")
      .isEqualTo("DMY")
      .jsonPath("$.capacity")
      .isEqualTo(0)
      .jsonPath("$.code")
      .isEqualTo(staffCode)
      .jsonPath("$.potentialCapacity")
      .isEqualTo(0)
  }
}
