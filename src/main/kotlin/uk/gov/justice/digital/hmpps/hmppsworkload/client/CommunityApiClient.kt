package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import java.math.BigInteger

class CommunityApiClient(private val webClient: WebClient) {

  fun getTeamStaff(teamCode: String): Mono<List<Staff>> {
    val responseType = object : ParameterizedTypeReference<List<Staff>>() {}
    return webClient
      .get()
      .uri("/teams/$teamCode/staff")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingTeamError("No team found for $teamCode")) }
      )
      .bodyToMono(responseType)
  }

  fun getStaffById(staffId: BigInteger): Mono<Staff> {
    return webClient
      .get()
      .uri("/staff/staffIdentifier/$staffId")
      .retrieve()
      .bodyToMono(Staff::class.java)
  }

  fun getStaffByCode(staffCode: String): Mono<Staff> {
    return webClient
      .get()
      .uri("/staff/staffCode/$staffCode")
      .retrieve()
      .bodyToMono(Staff::class.java)
  }

  fun getActiveConvictions(crn: String): Mono<List<Conviction>> {
    val responseType = object : ParameterizedTypeReference<List<Conviction>>() {}
    return webClient
      .get()
      .uri("/offenders/crn/$crn/convictions?activeOnly=true")
      .retrieve()
      .bodyToMono(responseType)
  }

  fun getSummaryByCrn(crn: String): Mono<PersonSummary> {
    return webClient
      .get()
      .uri("/offenders/crn/$crn")
      .retrieve()
      .bodyToMono(PersonSummary::class.java)
  }
}

class MissingTeamError(msg: String) : RuntimeException(msg)
