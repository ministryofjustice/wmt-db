package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.service.notify.SendEmailResponse
import java.util.concurrent.CompletableFuture

interface NotificationService {

  fun notifyAllocation(
    allocatedOfficer: Staff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    allocateCase: AllocateCase,
    allocatingOfficerUsername: String,
    token: String
  ): CompletableFuture<List<SendEmailResponse>>
}
