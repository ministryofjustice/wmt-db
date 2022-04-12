package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

import java.math.BigInteger

fun allocateCase(crn: String, eventId: BigInteger) = """
  {
     "crn": "$crn",
     "eventId": $eventId
  }
""".trimIndent()
