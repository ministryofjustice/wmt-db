package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
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

  fun choosePractitioners(crn: String, teamCodes: List<String>): Mono<ChoosePractitionerResponse> {
    return webClient
      .get()
      .uri("/allocation-demand/choose-practitioner?crn=$crn&teamCode=${teamCodes.joinToString(separator = ",")}")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingChoosePractitioner("No choose practitioner found for $crn $teamCodes")) }
      )
      .bodyToMono(ChoosePractitionerResponse::class.java)
      .onErrorResume { ex ->
        when (ex) {
          is MissingChoosePractitioner -> Mono.empty()
          else -> Mono.error(ex)
        }
      }
  }

  fun getPersonByCrn(crn: String): Mono<PersonSummary> {
    return getPerson(crn, "CRN")
      .onErrorResume { ex ->
        when (ex) {
          is MissingOffenderError -> Mono.just(PersonSummary(crn, Name("Unknown", "", "Unknown"), CaseType.UNKNOWN))
          else -> Mono.error(ex)
        }
      }
  }

  fun getPersonByNoms(noms: String): Mono<PersonSummary> {
    return getPerson(noms, "NOMS")
      .onErrorResume { ex ->
        when (ex) {
          is MissingOffenderError -> Mono.empty()
          else -> Mono.error(ex)
        }
      }
  }

  private fun getPerson(identifier: String, identifierType: String): Mono<PersonSummary> {
    return webClient
      .get()
      .uri("/person/$identifier?type=$identifierType")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingOffenderError("No offender by $identifierType found for : $identifier")) }
      )
      .bodyToMono(PersonSummary::class.java)
  }

  fun getOfficerView(staffCode: String): Mono<OfficerView> {
    return webClient
      .get()
      .uri("/staff/$staffCode/officer-view")
      .retrieve()
      .bodyToMono(OfficerView::class.java)
  }

  fun impact(crn: String, staffCode: String): Mono<ImpactResponse> = webClient
    .get()
    .uri("/allocation-demand/impact?crn=$crn&staff=$staffCode")
    .retrieve()
    .bodyToMono(ImpactResponse::class.java)

  fun allocationCompleteDetails(crn: String, eventNumber: String, staffCode: String): Mono<CompleteDetails> = webClient
    .get()
    .uri("/allocation-completed/details?crn=$crn&eventNumber=$eventNumber&staffCode=$staffCode")
    .retrieve()
    .bodyToMono(CompleteDetails::class.java)

  fun staffActiveCases(staffCode: String, crns: Collection<String>): Mono<StaffActiveCases> {
    val requestType = object : ParameterizedTypeReference<Collection<String>>() {}
    return webClient
      .post()
      .uri("/staff/$staffCode/active-cases")
      .body(Mono.just(crns), requestType)
      .retrieve()
      .bodyToMono(StaffActiveCases::class.java)
  }

  fun allocationDetails(crn: String, eventNumber: Int, staffCode: String, loggedInUser: String): Mono<AllocationDetails> =
    webClient
      .get()
      .uri("/allocation-demand/$crn/$eventNumber/allocation?staff=$staffCode&allocatingStaffUsername=$loggedInUser")
      .retrieve()
      .bodyToMono(AllocationDetails::class.java)
}

class MissingChoosePractitioner(msg: String) : RuntimeException(msg)
private class MissingOffenderError(msg: String) : RuntimeException(msg)
