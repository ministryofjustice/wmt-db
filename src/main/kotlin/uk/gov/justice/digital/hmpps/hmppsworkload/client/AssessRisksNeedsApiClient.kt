package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import java.util.Optional

@Component
class AssessRisksNeedsApiClient(@Qualifier("assessRiskNeedsApiWebClient")private val webClient: WebClient) {

  fun getRiskSummary(crn: String, token: String): Mono<Optional<RiskSummary>> {
    return webClient
      .get()
      .uri("/risks/crn/$crn/summary")
      .headers { it.setBearerAuth(token) }
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
}

private class MissingRiskError(msg: String) : RuntimeException(msg)
