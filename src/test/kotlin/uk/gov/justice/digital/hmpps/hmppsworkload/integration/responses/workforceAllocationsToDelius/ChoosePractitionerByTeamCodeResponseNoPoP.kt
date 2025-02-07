package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

fun choosePractitionerByTeamCodeResponseNoPoP() = """
{
  "teams": {
    "T1": [
      {
        "code": "OM1",
        "name": {
          "forename": "Jane",
          "surname": "Doe"
        },
        "email": "j.doe@email.co.uk",
        "grade": "PO"
      },
      {
        "code": "NOWORKLOAD1",
        "name": {
          "forename": "No",
          "surname": "Workload"
        },
        "email": "no.workload.email@co.uk"
      }
     ]
  }
}
""".trimIndent()
