package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

fun choosePractitionerByTeamCodesNoCommunityPersonManagerResponse() = """
{
  "crn": "CRN1",
  "name": {
    "forename": "Don",
    "middleName": "",
    "surname": "Cole"
  },
  "probationStatus": {
    "status": "PREVIOUSLY_MANAGED",
    "description": "Previously managed"
  },
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
     ],
    "T2": [
      {
        "code": "OM2",
        "name": {
          "forename": "Mark",
          "surname": "Reese"
        },
        "email": "",
        "grade": "PQiP"
      },
      {
        "code": "OM3",
        "name": {
          "forename": "Mark",
          "surname": "NoEmail"
        },
        "grade": "PQiP"
      }
    ]
  }
}
""".trimIndent()
