package uk.gov.justice.digital.hmpps.hmppsworkload.service

interface GetParoleReports {

  fun getParoleReports(staffCode: String, teamCode: String): Int
}
