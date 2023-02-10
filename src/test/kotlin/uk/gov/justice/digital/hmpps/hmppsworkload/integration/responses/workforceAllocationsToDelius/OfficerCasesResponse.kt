package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.ActiveCasesIntegration

fun deliusStaffActiveCasesResponse(staffCode: String, staffGrade: String, email: String?, activeCases: List<ActiveCasesIntegration>): String {
  val emailEntry = email?.let { "\"email\": \"$email\"," } ?: ""
  return """
  {
      "code": "$staffCode",
      "name": {
          "forename": "Sheila",
          "surname": "Hancock"
      },
      "grade": "$staffGrade",
      $emailEntry
      "cases": [
        ${activeCases.map { activeCaseDetail(it) }.joinToString(",")}
      ]
  }
  """.trimIndent()
}

private fun activeCaseDetail(activeCase: ActiveCasesIntegration) = """
  {
    "crn": "${activeCase.crn}",
    "name": {
        "forename": "${activeCase.firstName}",
        "middleName": "",
        "surname": "${activeCase.surname}"
    },
    "type": "${activeCase.type}"
  }
""".trimIndent()
