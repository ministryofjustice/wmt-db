package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

fun personSummaryResponse(crn: String, forename: String, middleName: String, surname: String, type: CaseType?): String {
  return """
{
  "crn": "$crn",
  "name": {
    "forename": "$forename",
    "middleName": "$middleName",
    "surname": "$surname"
  },
  "type": "$type"
}
  """.trimIndent()
}
