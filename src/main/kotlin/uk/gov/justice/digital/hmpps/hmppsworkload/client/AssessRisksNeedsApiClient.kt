package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import java.util.Optional

@Component
class AssessRisksNeedsApiClient(@Qualifier("assessRiskNeedsApiWebClient") private val webClient: WebClient) {

  fun getRiskSummary(crn: String, token: String): Mono<Optional<RiskSummary>> {
    return webClient
      .get()
      .uri("/risks/crn/$crn/summary")
      .header(HttpHeaders.AUTHORIZATION, token)
      .retrieve()
      .bodyToMono(RiskSummary::class.java)
      .retry(1)
      .map { Optional.of(it) }
      .onErrorResume {
        Mono.just(Optional.empty())
      }
  }

  fun getRiskPredictors(crn: String, token: String): Mono<List<RiskPredictor>> {
    val responseType = object : ParameterizedTypeReference<List<RiskPredictor>>() {}
    return webClient
      .get()
      .uri("/risks/crn/$crn/predictors/rsr/history")
      .header(HttpHeaders.AUTHORIZATION, token)
      .retrieve()
      .bodyToMono(responseType)
      .retry(1)
      .onErrorResume {
        Mono.just(emptyList())
      }
  }
}
