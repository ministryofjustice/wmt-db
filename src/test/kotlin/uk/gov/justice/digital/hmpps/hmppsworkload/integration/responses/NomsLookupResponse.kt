package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

class NomsLookupResponse

fun nomsLookupResponse(crn: String, nomsNumber: String) = """
  {
    "activeProbationManagedSentence": true,
    "contactDetails": {
      "allowSMS": true,
      "emailAddresses": [
        "string"
      ],
      "phoneNumbers": [
        {
          "number": "string",
          "type": "MOBILE"
        }
      ]
    },
    "currentDisposal": 1,
    "currentExclusion": true,
    "currentRestriction": true,
    "dateOfBirth": "1982-10-24",
    "firstName": "John",
    "gender": "Male",
    "middleNames": [
      "string"
    ],
    "offenderId": 0,
    "offenderProfile": {
      "disabilities": [
        {
          "disabilityId": 0,
          "disabilityType": {
            "code": "ABC123",
            "description": "Some description"
          },
          "endDate": "2022-11-04",
          "isActive": true,
          "lastUpdatedDateTime": "2020-09-20T11:00:00+01:00",
          "notes": "string",
          "provisions": [
            {
              "finishDate": "2022-11-04",
              "notes": "string",
              "provisionId": 0,
              "provisionType": {
                "code": "ABC123",
                "description": "Some description"
              },
              "startDate": "2022-11-04"
            }
          ],
          "startDate": "2022-11-04"
        }
      ],
      "ethnicity": "string",
      "genderIdentity": "Prefer to self-describe",
      "immigrationStatus": "string",
      "nationality": "string",
      "notes": "string",
      "offenderDetails": "string",
      "offenderLanguages": {
        "languageConcerns": "string",
        "otherLanguages": [
          "string"
        ],
        "primaryLanguage": "string",
        "requiresInterpreter": true
      },
      "previousConviction": {
        "convictionDate": "2022-11-04",
        "detail": {
          "additionalProp1": "string",
          "additionalProp2": "string",
          "additionalProp3": "string"
        }
      },
      "religion": "string",
      "remandStatus": "string",
      "riskColour": "string",
      "secondaryNationality": "string",
      "selfDescribedGender": "Jedi",
      "sexualOrientation": "string"
    },
    "otherIds": {
      "crn": "$crn",
      "croNumber": "123456/04A",
      "immigrationNumber": "A1234567",
      "mostRecentPrisonerNumber": "G12345",
      "niNumber": "AA112233B",
      "nomsNumber": "$nomsNumber",
      "pncNumber": "2004/0712343H"
    },
    "partitionArea": "National Data",
    "preferredName": "Bob",
    "previousSurname": "Davis",
    "softDeleted": true,
    "surname": "Smith",
    "title": "Mr"
  }
""".trimIndent()
