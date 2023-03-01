package uk.gov.justice.digital.hmpps.hmppsworkload.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class ResourceServerConfiguration {
  @Bean
  fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http
      .csrf { it.disable() }
      .authorizeExchange {
        it.pathMatchers(
          "/webjars/**",
          "/favicon.ico",
          "/health/**",
          "/info",
          "/v3/api-docs/**",
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/queue-admin/retry-all-dlqs"
        ).permitAll().anyExchange().authenticated()
      }
      .oauth2ResourceServer {
        it.jwt().jwtAuthenticationConverter(AuthAwareTokenConverter())
      }.build()
  }
}
