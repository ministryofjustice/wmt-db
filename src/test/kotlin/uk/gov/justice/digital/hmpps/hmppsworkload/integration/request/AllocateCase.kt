package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

fun allocateCase(crn: String, eventNumber: Int, sendEmailCopyToAllocatingOfficer: Boolean = true, evidenceContent: String? = null, evidenceSensitive: Boolean? = null) = """
  {
     "crn": "$crn",
     "emailTo" : ["additionalEmailReceiver@test.justice.gov.uk"],
     "sendEmailCopyToAllocatingOfficer": $sendEmailCopyToAllocatingOfficer,
     "eventNumber": $eventNumber
     ${evidenceContent?.let { ", \"evidenceContent\": \"$it\"" } ?: ""}
     ${evidenceSensitive?.let{ ", \"evidenceContentSensitive\": $it"} ?: ""}
  }
""".trimIndent()
