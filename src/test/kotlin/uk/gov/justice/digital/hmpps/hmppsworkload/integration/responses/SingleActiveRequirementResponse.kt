package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses

import java.math.BigInteger

fun singleActiveRequirementResponse(requirementId: BigInteger) = """
  {
      "requirements": [
          {
              "requirementId": $requirementId,
              "commencementDate": "2019-11-21",
              "startDate": "2019-11-21",
              "expectedStartDate": "2019-11-21",
              "createdDatetime": "2019-11-21T12:26:56",
              "active": true,
              "requirementTypeSubCategory": {
                  "code": "W01",
                  "description": "Regular"
              },
              "requirementTypeMainCategory": {
                  "code": "Y",
                  "description": "Supervision"
              },
              "length": 100,
              "lengthUnit": "Hours",
              "restrictive": false,
              "softDeleted": false
          }
      ]
  }
""".trimIndent()
