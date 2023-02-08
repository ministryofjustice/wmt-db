package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

fun allocationCompleteResponse() = """
  {
      "crn": "CRN1",
      "name": {
          "forename": "Shiva",
          "middleName": "",
          "surname": "Damon"
      },
      "event": {
          "number": "2"
      },
      "type": "COMMUNITY",
      "initialAppointment": {
          "date": "2023-01-09"
      },
      "staff": {
          "code": "OM1",
          "name": {
              "forename": "Jane",
              "surname": "Doe"
          },
          "email": "j.doe@email.co.uk",
          "grade": "PO"
      }
  }
""".trimIndent()
