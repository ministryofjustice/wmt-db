package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ChoosePractitionerResponse

class WorkforceAllocationsToDeliusApiClient(private val webClient: WebClient) {

  fun getPractitioner(crn: String, teamCode: List<String>): Mono<ChoosePractitionerResponse> {
    return webClient
      .get()
      .uri("/allocation-demand/choose-practitioner?crn=$crn&teamCode=${teamCode.joinToString(separator = ",")}")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingChoosePractitioner("No choose practitioner found for $crn $teamCode")) }
      )
      .bodyToMono(ChoosePractitionerResponse::class.java)
      .onErrorResume { ex ->
        when (ex) {
          is MissingChoosePractitioner -> Mono.empty()
          else -> Mono.error(ex)
        }
      }
  }
}

class MissingChoosePractitioner(msg: String) : RuntimeException(msg)
