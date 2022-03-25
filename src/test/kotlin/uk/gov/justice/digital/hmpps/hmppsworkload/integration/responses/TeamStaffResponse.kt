package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

fun teamStaffResponse() = """
  [
      {
          "staffCode": "OM1",
          "staffIdentifier": 123456789,
          "staff": {
              "forenames": "Ben",
              "surname": "Doe"
          }
      },
      {
          "staffCode": "OM2",
          "staffIdentifier": 234567891,
          "staff": {
              "forenames": "Sally",
              "surname": "Socks"
          }
      },
      {
          "staffCode": "OM3",
          "staffIdentifier": 987654321,
          "staff": {
              "forenames": "Jane",
              "surname": "Doe"
          }
      }
  ]
""".trimIndent()
