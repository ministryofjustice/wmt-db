package uk.gov.justice.digital.hmpps.hmppsworkload.service

interface GetInstitutionalReports {

  fun getInstitutionalReports(staffCode: String, teamCode: String): Int
}
