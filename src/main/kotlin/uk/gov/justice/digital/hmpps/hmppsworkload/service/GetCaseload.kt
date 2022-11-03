package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager

interface GetCaseload {
  fun getCases(personManager: PersonManager): List<Case>
}
