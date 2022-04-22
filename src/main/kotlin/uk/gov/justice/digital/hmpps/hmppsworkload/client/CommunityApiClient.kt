package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirements
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffSummary
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional

class CommunityApiClient(private val webClient: WebClient) {

  fun getTeamStaff(teamCode: String): Mono<List<StaffSummary>> {
    val responseType = object : ParameterizedTypeReference<List<StaffSummary>>() {}
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

  fun getStaffByUsername(username: String): Mono<Staff> {
    return webClient
      .get()
      .uri("/staff/username/$username")
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

  fun getActiveRequirements(crn: String, convictionId: BigInteger): Mono<ConvictionRequirements> {
    return webClient
      .get()
      .uri("/offenders/crn/$crn/convictions/$convictionId/requirements?activeOnly=true")
      .retrieve()
      .bodyToMono(ConvictionRequirements::class.java)
  }

  fun getInductionContacts(crn: String, contactDateFrom: LocalDate): Mono<List<Contact>> {
    val responseType = object : ParameterizedTypeReference<List<Contact>>() {}
    val contactDateFromQuery = contactDateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return webClient
      .get()
      .uri("/offenders/crn/$crn/contact-summary/inductions?contactDateFrom=$contactDateFromQuery")
      .retrieve()
      .bodyToMono(responseType)
  }

  fun getConviction(crn: String, convictionId: BigInteger): Mono<Optional<Conviction>> {
    return webClient
      .get()
      .uri("/offenders/crn/$crn/convictions/$convictionId")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingConvictionError("No conviction found for $crn and convictionId $convictionId")) }
      )
      .bodyToMono(Conviction::class.java)
      .map { Optional.of(it) }
      .onErrorResume { ex ->
        when (ex) {
          is MissingConvictionError -> Mono.just(Optional.empty())
          else -> Mono.error(ex)
        }
      }
  }
}

private class MissingConvictionError(msg: String) : RuntimeException(msg)
class MissingTeamError(msg: String) : RuntimeException(msg)
