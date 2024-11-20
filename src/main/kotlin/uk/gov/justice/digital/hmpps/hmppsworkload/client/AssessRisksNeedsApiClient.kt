package uk.gov.justice.digital.hmpps.hmppsworkload.client

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary

@Component
class AssessRisksNeedsApiClient(@Qualifier("assessRisksNeedsClientUserEnhancedAppScope") private val webClient: WebClient) {

  suspend fun getRiskSummary(crn: String): RiskSummary? {
    return webClient
      .get()
      .uri("/risks/crn/$crn/summary")
      .retrieve()
      .bodyToMono(RiskSummary::class.java)
      .retry(1)
      .onErrorResume {
        Mono.empty()
      }.awaitSingleOrNull()
  }

  suspend fun getRiskPredictors(crn: String): List<RiskPredictor> {
    val responseType = object : ParameterizedTypeReference<List<RiskPredictor>>() {}
    return webClient
      .get()
      .uri("/risks/crn/$crn/predictors/rsr/history")
      .retrieve()
      .bodyToMono(responseType)
      .retry(1)
      .onErrorResume {
        Mono.just(emptyList())
      }.awaitSingle()
  }
}
