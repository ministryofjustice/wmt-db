package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

fun staffByIdResponse(staffCode: String) = """
  {
    "email": "ben.doe@test.justice.gov.uk",
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
          "code": "string",
          "description": "string",
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
      "forenames": "Ben",
      "surname": "Doe"
    },
    "staffCode": "$staffCode",
    "staffGrade": {
      "code": "PSM",
      "description": "Some description"
    },
    "staffIdentifier": 123456789,
    "teams": [
      {
        "borough": {
          "code": "ABC123",
          "description": "Some description"
        },
        "code": "C01T04",
        "description": "OMU A",
        "district": {
          "code": "ABC123",
          "description": "Some description"
        },
        "emailAddress": "first.last@digital.justice.gov.uk",
        "endDate": "2022-03-29",
        "localDeliveryUnit": {
          "code": "ABC123",
          "description": "Some description"
        },
        "startDate": "2022-03-29",
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
