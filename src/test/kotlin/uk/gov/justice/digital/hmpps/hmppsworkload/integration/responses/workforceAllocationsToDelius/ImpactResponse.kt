package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

fun impactResponse(crn: String, staffCode: String, grade: String?): String {
  val gradeEntry = grade?.let { ",\"grade\": \"$grade\"" } ?: ""
  return """
  {
      "crn": "$crn",
      "name": {
          "forename": "Jonathon",
          "middleName": "",
          "surname": "Jones"
      },
      "staff": {
          "code": "$staffCode",
          "name": {
              "forename": "Sheila",
              "surname": "Hancock"
          }
          $gradeEntry
      }
  }
  """.trimIndent()
}
