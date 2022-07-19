package uk.gov.justice.digital.hmpps.hmppsworkload.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.OffenderSearchApiClient

@Configuration
class WebClientUserEnhancementConfiguration(
  @Value("\${community.endpoint.url}") private val communityApiRootUri: String,
  @Value("\${hmpps-tier.endpoint.url}") private val hmppsTierApiRootUri: String,
  @Value("\${offender-search.endpoint.url}") private val offenderSearchApiRootUri: String,
  @Value("\${bank-holiday-api.endpoint.url:https://www.gov.uk}") private val bankholidayApiRootUri: String,
  @Value("\${assess-risks-needs.endpoint.url}") private val assessRisksNeedsApiRootUri: String,
) {

  @Bean
  @RequestScope
  fun communityWebClientUserEnhancedAppScope(
    clientRegistrationRepository: ClientRegistrationRepository,
    builder: WebClient.Builder
  ): WebClient {
    return getOAuthWebClient(authorizedClientManagerUserEnhanced(clientRegistrationRepository), builder, communityApiRootUri, "community-api")
  }

  @Primary
  @Bean
  fun offenderSearchApiClientUserEnhanced(@Qualifier("offenderSearchWebClientUserEnhancedAppScope") webClient: WebClient): OffenderSearchApiClient {
    return OffenderSearchApiClient(webClient)
  }

  @Bean
  @RequestScope
  fun offenderSearchWebClientUserEnhancedAppScope(
    clientRegistrationRepository: ClientRegistrationRepository,
    builder: WebClient.Builder
  ): WebClient {
    return getOAuthWebClient(authorizedClientManagerUserEnhanced(clientRegistrationRepository), builder, offenderSearchApiRootUri, "offender-search-api")
  }

  @Primary
  @Bean
  fun communityApiClientUserEnhanced(@Qualifier("communityWebClientUserEnhancedAppScope") webClient: WebClient): CommunityApiClient {
    return CommunityApiClient(webClient)
  }

  @Bean
  @RequestScope
  fun hmppsTierWebClientUserEnhancedAppScope(
    clientRegistrationRepository: ClientRegistrationRepository,
    builder: WebClient.Builder
  ): WebClient {
    return getOAuthWebClient(authorizedClientManagerUserEnhanced(clientRegistrationRepository), builder, hmppsTierApiRootUri, "hmpps-tier-api")
  }

  @Primary
  @Bean
  fun hmppsTierApiClientUserEnhanced(@Qualifier("hmppsTierWebClientUserEnhancedAppScope") webClient: WebClient): HmppsTierApiClient {
    return HmppsTierApiClient(webClient)
  }

  private fun authorizedClientManagerUserEnhanced(clients: ClientRegistrationRepository?): OAuth2AuthorizedClientManager {
    val service: OAuth2AuthorizedClientService = InMemoryOAuth2AuthorizedClientService(clients)
    val manager = AuthorizedClientServiceOAuth2AuthorizedClientManager(clients, service)

    val defaultClientCredentialsTokenResponseClient = DefaultClientCredentialsTokenResponseClient()

    val authentication = SecurityContextHolder.getContext().authentication

    defaultClientCredentialsTokenResponseClient.setRequestEntityConverter { grantRequest: OAuth2ClientCredentialsGrantRequest ->
      val converter = CustomOAuth2ClientCredentialsGrantRequestEntityConverter()
      val username = authentication.name
      converter.enhanceWithUsername(grantRequest, username)
    }

    val authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
      .clientCredentials { clientCredentialsGrantBuilder: OAuth2AuthorizedClientProviderBuilder.ClientCredentialsGrantBuilder ->
        clientCredentialsGrantBuilder.accessTokenResponseClient(defaultClientCredentialsTokenResponseClient)
      }
      .build()

    manager.setAuthorizedClientProvider(authorizedClientProvider)
    return manager
  }

  private fun getOAuthWebClient(
    authorizedClientManager: OAuth2AuthorizedClientManager,
    builder: WebClient.Builder,
    rootUri: String,
    registrationId: String
  ): WebClient {
    val oauth2Client = ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
    oauth2Client.setDefaultClientRegistrationId(registrationId)
    return builder.baseUrl(rootUri)
      .apply(oauth2Client.oauth2Configuration())
      .build()
  }

  @Bean
  fun bankHolidayApiWebClient(builder: WebClient.Builder): WebClient = builder.baseUrl(bankholidayApiRootUri).build()

  @Bean
  fun assessRiskNeedsApiWebClient(builder: WebClient.Builder): WebClient = builder.baseUrl(assessRisksNeedsApiRootUri).build()
}
