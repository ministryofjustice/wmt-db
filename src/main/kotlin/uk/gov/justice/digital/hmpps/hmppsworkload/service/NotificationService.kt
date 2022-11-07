package uk.gov.justice.digital.hmpps.hmppsworkload.service

import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.service.notify.SendEmailResponse

interface NotificationService {

  fun notifyAllocation(
    allocatedOfficer: DeliusStaff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    allocateCase: AllocateCase,
    allocatingOfficerUsername: String,
    token: String
  ): Mono<List<SendEmailResponse>>
}
