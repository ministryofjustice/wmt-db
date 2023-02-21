package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

fun allocateCase(crn: String, eventNumber: Int, sendEmailCopyToAllocatingOfficer: Boolean = true) = """
  {
     "crn": "$crn",
     "emailTo" : ["additionalEmailReceiver@test.justice.gov.uk"],
     "sendEmailCopyToAllocatingOfficer": $sendEmailCopyToAllocatingOfficer,
     "eventNumber": $eventNumber,
     "eventId": 3145315
  }
""".trimIndent()
