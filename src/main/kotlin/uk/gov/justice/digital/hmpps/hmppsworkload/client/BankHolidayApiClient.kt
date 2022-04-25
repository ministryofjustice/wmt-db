package uk.gov.justice.digital.hmpps.hmppsworkload.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.BankHolidays
import uk.gov.justice.digital.hmpps.hmppsworkload.config.CacheConfiguration

@Service
class BankHolidayApiClient(@Qualifier("bankHolidayApiWebClient") private val webClient: WebClient) {
  private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

  @Cacheable(CacheConfiguration.BANK_HOLIDAYS_CACHE_NAME)
  fun getBankHolidays(): BankHolidays {
    return webClient.get()
      .uri("/bank-holidays.json")
      .retrieve()
      .bodyToMono(typeReference<BankHolidays>())
      .block()!!
  }
}
