package uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius

import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.AllocationDetailIntegration

fun allocationDetailsResponse(allocationDetails: List<AllocationDetailIntegration>): String = """
  {
     "cases": [
        ${allocationDetails.joinToString(",") { allocationDetail(it) }}
     ]
  }
""".trimIndent()

private fun allocationDetail(allocationDetail: AllocationDetailIntegration) = """
  {
    "crn": "${allocationDetail.crn}",
    "name": {
        "forename": "Dylan",
        "middleName": "Adam",
        "surname": "Armstrong"
      },
      "staff": {
        "code": "${allocationDetail.staffCode}",
        "name": {
          "forename": "Sally",
          "middleName": "",
          "surname": "Smith"
        },
        "grade": "PO"
      }
  }
""".trimIndent()
