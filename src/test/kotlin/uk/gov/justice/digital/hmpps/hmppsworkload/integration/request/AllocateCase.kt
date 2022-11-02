package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

import java.math.BigInteger

fun allocateCase(crn: String, eventId: BigInteger, emailCopyHasValue: Boolean = false): String {
  var emailCopy = ""
  if (emailCopyHasValue) emailCopy = ",\"emailCopy\": \"no\""
  return """
  {
     "crn": "$crn",
     "eventId": $eventId,
     "emailTo" : ["additionalEmailReciever@test.justice.gov.uk"]
     $emailCopy
  }
  """.trimIndent()
}
