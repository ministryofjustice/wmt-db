package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport

interface GetCourtReports {

  fun getCourtReports(staffCode: String, teamCode: String): List<CourtReport>
}
