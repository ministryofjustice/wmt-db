package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

fun notFoundTierResponse() = """
  {
  "status": 404,
  "developerMessage": "Tier Not Found"
  }
""".trimIndent()
