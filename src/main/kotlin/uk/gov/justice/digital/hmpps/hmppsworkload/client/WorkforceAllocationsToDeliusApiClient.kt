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

  fun getSummaryByCrn(crn: String): Mono<PersonSummary> {
    return webClient
      .get()
      .uri("/person/$crn")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.FORBIDDEN == httpStatus },
        { Mono.error(ForbiddenOffenderError("Unable to access offender details for $crn")) }
      )
      .bodyToMono(PersonSummary::class.java)
      .onErrorResume { ex ->
        when (ex) {
          is ForbiddenOffenderError -> Mono.just(PersonSummary("Restricted Access", Name("Restricted", "", "Access",), CaseType.UNKNOWN))
          else -> Mono.error(ex)
        }
      }
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
private class ForbiddenOffenderError(msg: String) : RuntimeException(msg)
