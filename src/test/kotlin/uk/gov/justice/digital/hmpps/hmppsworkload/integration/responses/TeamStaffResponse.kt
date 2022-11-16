package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

fun teamStaffJsonResponse(staffCode: String) = """
  [
      {
          "email": "some.email@justice.gov.uk",
          "staffCode": "$staffCode",
          "staffIdentifier": 123456789,
          "staff": {
              "forenames": "Jane",
              "surname": "Doe"
          },
          "staffGrade": {
            "code": "PSM",
            "description": "Some description"
          }
      },
      {
          "staffCode": "OM2",
          "staffIdentifier": 234567891,
          "staff": {
              "forenames": "Sally",
              "surname": "Socks"
          },
          "staffGrade": {
            "code": "PSP",
            "description": "Some description"
          }
      },
      {
          "email": "no.workload.email@justice.gov.uk",
          "staffCode": "NOWORKLOAD1",
          "staffIdentifier": 987654321,
          "staff": {
              "forenames": "No",
              "surname": "Workload"
          }
      },
      {
          "staffCode": "OM3",
          "staffIdentifier": 234567891,
          "staff": {
              "forenames": "Billy",
              "surname": "Smith"
          },
          "staffGrade": {
            "code": "PSP",
            "description": "Some description"
          }
          
      }
  ]
""".trimIndent()
