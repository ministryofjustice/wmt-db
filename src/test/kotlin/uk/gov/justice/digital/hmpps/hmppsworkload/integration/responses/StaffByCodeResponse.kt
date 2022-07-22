package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

fun staffByCodeResponse(staffCode: String, teamCode: String, staffGrade: String) = """
  {
    "email": "sheila.hancock@test.justice.gov.uk",
    "probationArea": {
      "code": "N01",
      "description": "NPS North West",
      "institution": {
        "code": "string",
        "description": "string",
        "establishmentType": {
          "code": "ABC123",
          "description": "Some description"
        },
        "institutionId": 0,
        "institutionName": "string",
        "isEstablishment": true,
        "isPrivate": true,
        "nomsPrisonInstitutionCode": "string"
      },
      "nps": true,
      "organisation": {
        "code": "ABC123",
        "description": "Some description"
      },
      "probationAreaId": 0,
      "teams": [
        {
          "borough": {
            "code": "ABC123",
            "description": "Some description"
          },
          "code": "$teamCode",
          "description": "Test Team",
          "district": {
            "code": "ABC123",
            "description": "Some description"
          },
          "externalProvider": {
            "code": "ABC123",
            "description": "Some description"
          },
          "isPrivate": true,
          "localDeliveryUnit": {
            "code": "ABC123",
            "description": "Some description"
          },
          "name": "string",
          "providerTeamId": 0,
          "scProvider": {
            "code": "ABC123",
            "description": "Some description"
          },
          "teamId": 0
        }
      ]
    },
    "staff": {
      "forenames": "Sheila",
      "surname": "Hancock"
    },
    "staffCode": "$staffCode",
    "staffGrade": {
      "code": "$staffGrade",
      "description": "Some description"
    },
    "staffIdentifier": 123456,
    "teams": [
      {
        "borough": {
          "code": "ABC123",
          "description": "Some description"
        },
        "code": "$teamCode",
        "description": "Test Team",
        "district": {
          "code": "ABC123",
          "description": "Some description"
        },
        "emailAddress": "first.last@digital.justice.gov.uk",
        "endDate": "2022-04-04",
        "localDeliveryUnit": {
          "code": "ABC123",
          "description": "Some description"
        },
        "startDate": "2022-04-04",
        "teamType": {
          "code": "ABC123",
          "description": "Some description"
        },
        "telephone": "OMU A"
      }
    ],
    "telephoneNumber": "020 1111 2222",
    "username": "SheilaHancockNPS"
  }
""".trimIndent()
