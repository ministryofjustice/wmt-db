package uk.gov.justice.digital.hmpps.hmppsworkload.client

import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToFlow
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary

@Component
class AssessRisksNeedsApiClient(@Qualifier("assessRiskNeedsApiWebClient") private val webClient: WebClient) {

  suspend fun getRiskSummary(crn: String): RiskSummary? {
    repeat(2) {
      try {
        return webClient
          .get()
          .uri("/risks/crn/$crn/summary")
          .retrieve()
          .awaitBody()
      } catch (e: WebClientResponseException) {
        log.error("retrying retrieving risk summary", e)
      }
    }
    return null
  }

  suspend fun getRiskPredictors(crn: String): List<RiskPredictor> {
    repeat(2) {
      try {
        return webClient
          .get()
          .uri("/risks/crn/$crn/predictors/rsr/history")
          .retrieve()
          .bodyToFlow<RiskPredictor>()
          .toList()
      } catch (e: WebClientResponseException) {
        log.error("retrying retrieving risk predictors", e)
      }
    }
    return emptyList()
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
