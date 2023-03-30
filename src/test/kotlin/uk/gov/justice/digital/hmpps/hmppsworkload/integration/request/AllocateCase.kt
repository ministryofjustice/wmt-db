package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

fun allocateCase(crn: String, eventNumber: Int, sendEmailCopyToAllocatingOfficer: Boolean = true, allocationJustificationNotes: String? = null, sensitiveNotes: Boolean? = null) = """
  {
     "crn": "$crn",
     "emailTo" : ["additionalEmailReceiver@test.justice.gov.uk"],
     "sendEmailCopyToAllocatingOfficer": $sendEmailCopyToAllocatingOfficer,
     "eventNumber": $eventNumber
     ${allocationJustificationNotes?.let { ", \"allocationJustificationNotes\": \"$it\"" } ?: ""}
     ${sensitiveNotes?.let{ ", \"sensitiveNotes\": $it"} ?: ""}
  }
""".trimIndent()
