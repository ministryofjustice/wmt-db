package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

fun allocateCase(crn: String) = """
  {
     "crn": "$crn"
  }
""".trimIndent()
