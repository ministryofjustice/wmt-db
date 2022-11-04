package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirements
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderAssessment
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.TeamStaff
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional

class CommunityApiClient(private val webClient: WebClient) {

  fun getTeamStaff(teamCode: String): Mono<List<TeamStaff>> {
    val responseType = object : ParameterizedTypeReference<List<TeamStaff>>() {}
    return webClient
      .get()
      .uri("/teams/$teamCode/staff")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingTeamError("No team found for $teamCode")) }
      )
      .bodyToMono(responseType)
      .onErrorResume { ex ->
        when (ex) {
          is MissingTeamError -> Mono.empty()
          else -> Mono.error(ex)
        }
      }
  }

  fun getStaffByUsername(username: String): Mono<Staff> {
    return webClient
      .get()
      .uri("/staff/username/{username}", username)
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingStaffError("User is not a staff member $username")) }
      )
      .bodyToMono(Staff::class.java)
  }

  fun <T> getStaffByCode(staffCode: String, clazz: Class<T>): Mono<T> {
    return webClient
      .get()
      .uri("/staff/staffCode/$staffCode")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingStaffError("staff member not found at $staffCode")) }
      )
      .bodyToMono(clazz)
  }

  fun getActiveConvictions(crn: String): Mono<List<Conviction>> {
    val responseType = object : ParameterizedTypeReference<List<Conviction>>() {}
    return webClient
      .get()
      .uri("/offenders/crn/$crn/convictions?activeOnly=true")
      .retrieve()
      .bodyToMono(responseType)
  }

  fun getAllConvictions(crn: String): Mono<List<Conviction>> {
    val responseType = object : ParameterizedTypeReference<List<Conviction>>() {}

    return webClient
      .get()
      .uri("/offenders/crn/$crn/convictions")
      .retrieve()
      .bodyToMono(responseType)
  }

  fun getAllConvictionsFlux(crn: String): Flux<Conviction> = webClient
    .get()
    .uri("/offenders/crn/$crn/convictions")
    .retrieve()
    .bodyToFlux(Conviction::class.java)

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

  fun getActiveRequirements(crn: String, convictionId: BigInteger): Mono<ConvictionRequirements> {
    return webClient
      .get()
      .uri { uriBuilder ->
        uriBuilder.path("/offenders/crn/{crn}/convictions/{convictionId}/requirements")
          .queryParam("activeOnly", true)
          .queryParam("excludeSoftDeleted", true)
          .build(crn, convictionId)
      }
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

  fun getAssessment(crn: String): Mono<Optional<OffenderAssessment>> {
    return webClient
      .get()
      .uri("/offenders/crn/$crn/assessments")
      .retrieve()
      .onStatus(
        { httpStatus -> HttpStatus.NOT_FOUND == httpStatus },
        { Mono.error(MissingOffenderAssessmentError("No offender assessment found for $crn")) }
      )
      .bodyToMono(OffenderAssessment::class.java)
      .map { Optional.of(it) }
      .onErrorResume { ex ->
        when (ex) {
          is MissingOffenderAssessmentError -> Mono.just(Optional.empty())
          else -> Mono.error(ex)
        }
      }
  }
}

class MissingTeamError(msg: String) : RuntimeException(msg)
class MissingStaffError(msg: String) : RuntimeException(msg)
private class MissingOffenderAssessmentError(msg: String) : RuntimeException(msg)

private class ForbiddenOffenderError(msg: String) : RuntimeException(msg)
