package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CourtReport
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManagerIdentifier

interface GetCourtReports {

  fun getCourtReports(personManagerIdentifier: PersonManagerIdentifier): List<CourtReport>
}
