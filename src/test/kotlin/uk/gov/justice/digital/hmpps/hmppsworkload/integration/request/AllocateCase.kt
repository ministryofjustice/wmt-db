package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

import java.math.BigInteger

fun allocateCase(crn: String, eventId: BigInteger, sendEmailCopyToAllocatingOfficer: Boolean = true) = """
  {
     "crn": "$crn",
     "eventId": $eventId,
     "emailTo" : ["additionalEmailReceiver@test.justice.gov.uk"],
     "sendEmailCopyToAllocatingOfficer": $sendEmailCopyToAllocatingOfficer
  }
""".trimIndent()
