package uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain

data class ActiveCasesIntegration(
  val crn: String,
  val firstName: String,
  val surname: String,
  val type: String,
)
