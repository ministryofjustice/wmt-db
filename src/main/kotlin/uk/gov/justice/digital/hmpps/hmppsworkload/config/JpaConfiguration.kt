package uk.gov.justice.digital.hmpps.hmppsworkload.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.ZonedDateTime
import java.util.Optional

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
class JpaConfiguration {

  @Bean
  fun auditingDateTimeProvider(): DateTimeProvider = DateTimeProvider { Optional.of(ZonedDateTime.now()) }
}
