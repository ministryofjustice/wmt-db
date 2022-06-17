package uk.gov.justice.digital.hmpps.hmppsworkload.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.gov.justice.digital.hmpps.hmppsworkload.client.BankHolidayApiClient

@Configuration
@EnableCaching
class CacheConfiguration {

  @Bean
  fun cacheManager(): CacheManager {
    return ConcurrentMapCacheManager(BANK_HOLIDAYS_CACHE_NAME)
  }

  @Bean
  fun populateBankHolidayCache(bankHolidayApiClient: BankHolidayApiClient): CommandLineRunner {
    return CommandLineRunner {
      log.info("populating bank holiday cache")
      bankHolidayApiClient.getBankHolidays()
    }
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    const val BANK_HOLIDAYS_CACHE_NAME: String = "bankHolidays"
  }
}
