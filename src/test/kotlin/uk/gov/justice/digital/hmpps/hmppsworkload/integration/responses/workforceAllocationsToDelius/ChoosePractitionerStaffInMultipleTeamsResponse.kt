package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

fun choosePractitionerStaffInMultipleTeamsResponse() = """
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
    "communityPersonManager": {
      "code": "N03A019",
      "name": {
        "forename": "Derek",
        "surname": "Pint"
      },
      "teamCode": "N03F01",
      "grade": "PO"
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
        }
       ],
      "T2": [
        {
          "code": "OM1",
          "name": {
            "forename": "Jane",
            "surname": "Doe"
          },
          "email": "j.doe@email.co.uk",
          "grade": "PO"
        }
      ]
    }
  }
""".trimIndent()
