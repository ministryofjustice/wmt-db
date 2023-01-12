package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ChoosePractitionerResponse

class WorkforceAllocationsToDeliusApiClient(private val webClient: WebClient) {

  fun getPractitioner(crn: String, teamCode: List<String>): Mono<ChoosePractitionerResponse> {
    return webClient
      .get()
      .uri("/allocation-demand/choose-practitioner?crn=$crn&teamCode=${teamCode.joinToString(separator = ",")}")
      .header(HttpHeaders.AUTHORIZATION)
      .retrieve()
      .bodyToMono(ChoosePractitionerResponse::class.java)
  }
}
