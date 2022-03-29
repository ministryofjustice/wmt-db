package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

fun impactCase(crn: String) = """
  {
     "crn": "$crn",
     "convictionId": 123456789
  }
""".trimIndent()
