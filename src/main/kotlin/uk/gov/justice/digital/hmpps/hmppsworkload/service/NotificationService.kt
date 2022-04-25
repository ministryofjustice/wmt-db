package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import java.math.BigInteger

interface NotificationService {

  fun notifyAllocation(
    allocatedOfficer: Staff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    crn: String,
    convictionId: BigInteger,
    allocateCase: AllocateCase,
    allocatingOfficerUsername: String,
    teamCode: String
  )
}
