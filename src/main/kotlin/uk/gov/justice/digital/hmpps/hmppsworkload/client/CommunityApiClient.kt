package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderDetails

class CommunityApiClient(private val webClient: WebClient) {

  fun getCrn(nomsNumber: String): Mono<String> {
    return webClient.get()
      .uri("/secure/offenders/nomsNumber/$nomsNumber")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingOffenderError("No offender  found for $nomsNumber")) }
      )
      .bodyToMono(OffenderDetails::class.java)
      .map { it.otherIds.crn }
      .onErrorResume { ex ->
        when (ex) {
          is MissingOffenderError -> Mono.empty()
          else -> Mono.error(ex)
        }
      }
  }
}

private class MissingOffenderError(msg: String) : RuntimeException(msg)
