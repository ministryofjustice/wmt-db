package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

fun deliusAllocationResponse(crn: String, staffCode: String, allocateToEmail: String) = """
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
      },
      "email": "$allocateToEmail",
      "grade": "PO"
    },
    "allocatingStaff": {
      "code": "STAFF1",
      "name": {
        "forename": "Sheila",
        "surname": "Hancock"
      },
      "email": "sheila.hancock@test.justice.gov.uk",
      "grade": "SPO"
    },
    "ogrs": {
      "lastUpdatedDate": "2022-11-04",
      "score": 89
    },
    "sentence": {
      "description": "ORA Adult Custody (inc PSS)",
      "code": "SC",
      "date": "2022-10-07T00:00:00+01:00",
      "length": "48 Weeks"
    },
    "court": {
      "name": "The Magistrates Court",
      "appearanceDate": "2022-10-07"
    },
    "offences": [
      {
        "mainOffence": true,
        "mainCategory": "Abstracting Electricity"
      }
    ],
    "activeRequirements": [
      {
        "mainCategory":"Rehabilitation Activity Requirement (RAR)",
        "subCategory":"Rehabilitation Activity Requirement (RAR)",
        "length":"20 Days",
        "id":645234212,
         "manager": {
             "code": "red",
             "name": {
               "forename": "Jess",
               "middleName": "",
               "surname": "wilkins"
             },
             "teamCode": "Mercedes",
             "grade": "Pass",
             "allocated": true
         },
         "isUnpaidWork": true
       }]
  }
""".trimIndent()
