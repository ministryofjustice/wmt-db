package uk.gov.justice.digital.hmpps.hmppsworkload.integration.client

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.hmppsworkload.client.BankHolidayApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase

class BankHolidayApiClientCache : IntegrationTestBase() {

  @Autowired
  protected lateinit var bankHolidayApiClient: BankHolidayApiClient

  @Test
  fun `cache is always called after startup`() {
    bankHolidayApiClient.getBankHolidays()

    bankHolidayApi.verifyZeroInteractions()
  }
}
