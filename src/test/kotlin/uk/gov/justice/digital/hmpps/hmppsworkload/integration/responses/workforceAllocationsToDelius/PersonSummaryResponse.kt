package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

fun personSummaryResponse(crn: String, type: CaseType?): String = """
{
  "crn": "$crn",
  "name": {
    "forename": "Jane",
    "middleName": "",
    "surname": "Doe"
  },
  "type": "$type"
}
""".trimIndent()
