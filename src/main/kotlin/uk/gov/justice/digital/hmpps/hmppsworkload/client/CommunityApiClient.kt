package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary

class CommunityApiClient(private val webClient: WebClient) {

  fun getActiveConvictions(crn: String): Flux<Conviction> {
    return webClient
      .get()
      .uri("/offenders/crn/$crn/convictions?activeOnly=true")
      .retrieve()
      .bodyToFlux(Conviction::class.java)
  }

  fun getSummaryByCrn(crn: String): Mono<PersonSummary> {
    return webClient
      .get()
      .uri("/offenders/crn/$crn")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.FORBIDDEN == httpStatus },
        { Mono.error(ForbiddenOffenderError("Unable to access offender details for $crn")) }
      )
      .bodyToMono(PersonSummary::class.java)
      .onErrorResume { ex ->
        when (ex) {
          is ForbiddenOffenderError -> Mono.just(PersonSummary("Restricted", "Access"))
          else -> Mono.error(ex)
        }
      }
  }

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

private class ForbiddenOffenderError(msg: String) : RuntimeException(msg)

private class MissingOffenderError(msg: String) : RuntimeException(msg)
