package uk.gov.justice.digital.hmpps.hmppsworkload.client

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class HmppsTierApiClient(private val webClient: WebClient) {

  fun getTierByCrn(crn: String): Mono<String> = webClient
    .get()
    .uri("/crn/$crn/tier")
    .retrieve()
    .onStatus(
      { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
      { Mono.error(MissingTierError("No tier found for $crn")) }
    )
    .bodyToMono(TierDto::class.java)
    .map { it.tierScore }
    .onErrorResume { ex ->
      when (ex) {
        is MissingTierError -> Mono.empty()
        else -> Mono.error(ex)
      }
    }
}

private data class TierDto @JsonCreator constructor(
  @JsonProperty("tierScore")
  val tierScore: String
)

private class MissingTierError(msg: String) : RuntimeException(msg)
