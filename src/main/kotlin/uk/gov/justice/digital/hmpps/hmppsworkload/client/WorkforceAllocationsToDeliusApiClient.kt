package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchangeOrNull
import org.springframework.web.reactive.function.client.createExceptionAndAwait
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.AllocationDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ChoosePractitionerResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.CompleteDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ImpactResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Name
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OfficerView
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffActiveCases
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

class WorkforceAllocationsToDeliusApiClient(private val webClient: WebClient) {

  suspend fun choosePractitioners(crn: String, teamCodes: List<String>): ChoosePractitionerResponse? {
    return webClient
      .get()
      .uri("/allocation-demand/choose-practitioner?crn=$crn&teamCode=${teamCodes.joinToString(separator = ",")}")
      .awaitExchangeOrNull { response ->
        when (response.statusCode()) {
          HttpStatus.OK -> response.awaitBody()
          HttpStatus.NOT_FOUND -> null
          else -> throw response.createExceptionAndAwait()
        }
      }
  }

  suspend fun getPersonByCrn(crn: String): PersonSummary? {
    return getPerson(crn, "CRN") { response ->
      when (response.statusCode()) {
        HttpStatus.OK -> response.awaitBody()
        HttpStatus.NOT_FOUND -> PersonSummary(crn, Name("Unknown", "", "Unknown"), CaseType.UNKNOWN)
        else -> throw response.createExceptionAndAwait()
      }
    }
  }

  suspend fun getPersonByNoms(noms: String): PersonSummary? {
    return getPerson(noms, "NOMS") { response ->
      when (response.statusCode()) {
        HttpStatus.OK -> response.awaitBody()
        HttpStatus.NOT_FOUND -> null
        else -> throw response.createExceptionAndAwait()
      }
    }
  }

  private suspend fun getPerson(identifier: String, identifierType: String, responseHandler: suspend (ClientResponse) -> PersonSummary?): PersonSummary? {
    return webClient
      .get()
      .uri("/person/$identifier?type=$identifierType")
      .awaitExchangeOrNull(responseHandler)
  }

  suspend fun getOfficerView(staffCode: String): OfficerView {
    return webClient
      .get()
      .uri("/staff/$staffCode/officer-view")
      .retrieve()
      .awaitBody()
  }

  suspend fun impact(crn: String, staffCode: String): ImpactResponse = webClient
    .get()
    .uri("/allocation-demand/impact?crn=$crn&staff=$staffCode")
    .retrieve()
    .awaitBody()

  suspend fun allocationCompleteDetails(crn: String, eventNumber: String, staffCode: String): CompleteDetails = webClient
    .get()
    .uri("/allocation-completed/details?crn=$crn&eventNumber=$eventNumber&staffCode=$staffCode")
    .retrieve()
    .awaitBody()

  suspend fun staffActiveCases(staffCode: String, crns: Collection<String>): StaffActiveCases {
    val requestType = object : ParameterizedTypeReference<Collection<String>>() {}
    return webClient
      .post()
      .uri("/staff/$staffCode/active-cases")
      .body(Mono.just(crns), requestType)
      .retrieve()
      .awaitBody()
  }

  suspend fun allocationDetails(crn: String, eventNumber: Int, staffCode: String, loggedInUser: String): AllocationDetails =
    webClient
      .get()
      .uri("/allocation-demand/$crn/$eventNumber/allocation?staff=$staffCode&allocatingStaffUsername=$loggedInUser")
      .retrieve()
      .awaitBody()
}
