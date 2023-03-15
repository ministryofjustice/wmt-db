package uk.gov.justice.digital.hmpps.hmppsworkload.integration.request

import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.AllocationDetailIntegration

fun allocationDetailsRequest(allocationDetails: List<AllocationDetailIntegration>): String = """
  {
    "cases": [
      ${allocationDetails.joinToString(",") { allocationDetail(it) }}
    ]
  }
""".trimIndent()

private fun allocationDetail(allocationDetail: AllocationDetailIntegration) = """
  {
    "crn": "${allocationDetail.crn}",
    "staffCode": "${allocationDetail.staffCode}"
  }
""".trimIndent()
