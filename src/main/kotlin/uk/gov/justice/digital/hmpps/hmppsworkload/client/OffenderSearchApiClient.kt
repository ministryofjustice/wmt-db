package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderDetails

class OffenderSearchApiClient(private val webClient: WebClient) {

  fun getOffendersByCrns(crns: List<String>): Mono<List<OffenderDetails>> {
    val responseType = object : ParameterizedTypeReference<List<OffenderDetails>>() {}
    return webClient
      .post()
      .uri("/crns")
      .bodyValue(crns)
      .retrieve()
      .bodyToMono(responseType)
  }
}
