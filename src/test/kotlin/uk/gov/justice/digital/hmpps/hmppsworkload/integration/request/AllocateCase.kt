package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

fun allocateCase(crn: String, eventNumber: Int, sendEmailCopyToAllocatingOfficer: Boolean = true, allocationJustificationNotes: String = "some notes", sensitiveNotes: Boolean = false, spoOversightNotes:String = "spo notes", sensitiveOversightNotes:Boolean? = null) = """
  {
     "crn": "$crn",
     "emailTo" : ["additionalEmailReceiver@test.justice.gov.uk"],
     "sendEmailCopyToAllocatingOfficer": $sendEmailCopyToAllocatingOfficer,
     "eventNumber": $eventNumber,
     "spoOversightNotes": $spoOversightNotes,
     "sensitiveOversightNotes": $sensitiveOversightNotes
  }
""".trimIndent()
