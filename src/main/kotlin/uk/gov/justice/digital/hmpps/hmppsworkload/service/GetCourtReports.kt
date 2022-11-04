package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier

interface GetCourtReports {

  fun getCourtReports(staffIdentifier: StaffIdentifier): List<CourtReport>
}
