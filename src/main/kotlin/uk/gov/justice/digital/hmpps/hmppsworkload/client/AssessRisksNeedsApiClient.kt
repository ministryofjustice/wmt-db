package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import java.util.Optional

@Component
class AssessRisksNeedsApiClient(@Qualifier("assessRiskNeedsApiWebClient")private val webClient: WebClient) {

  fun getRiskSummary(crn: String, token: String): Mono<Optional<RiskSummary>> {
    return webClient
      .get()
      .uri("/risks/crn/$crn/summary")
      .header(HttpHeaders.AUTHORIZATION, token)
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingRiskError("No risk summary found for $crn")) }
      )
      .bodyToMono(RiskSummary::class.java)
      .map { Optional.of(it) }
      .onErrorResume { ex ->
        when (ex) {
          is MissingRiskError -> Mono.just(Optional.empty())
          else -> Mono.error(ex)
        }
      }
  }

  fun getRiskPredictors(crn: String, token: String): Mono<List<RiskPredictor>> {
    val responseType = object : ParameterizedTypeReference<List<RiskPredictor>>() {}
    return webClient
      .get()
      .uri("/risks/crn/$crn/predictors/rsr/history")
      .header(HttpHeaders.AUTHORIZATION, token)
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingRiskError("No risk predictors found for $crn")) }
      )
      .bodyToMono(responseType)
      .onErrorResume { ex ->
        when (ex) {
          is MissingRiskError -> Mono.just(emptyList())
          else -> Mono.error(ex)
        }
      }
  }
}

private class MissingRiskError(msg: String) : RuntimeException(msg)
