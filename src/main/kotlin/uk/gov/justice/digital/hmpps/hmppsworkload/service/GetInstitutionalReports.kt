package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.InstitutionalReport

interface GetInstitutionalReports {

  fun getInstitutionalReports(staffCode: String, teamCode: String): List<InstitutionalReport>
}
