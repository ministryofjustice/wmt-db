package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.service.notify.NotificationClientApi
import java.math.BigInteger

@Service
class EmailNotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
) : NotificationService {

  override fun notifyAllocation(
    allocatedOfficer: Staff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    crn: String,
    convictionId: BigInteger
  ) {
    val parameters = mapOf(
      "case_name" to "${personSummary.firstName} ${personSummary.surname}",
      "crn" to crn,
      "officer_name" to "${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}",
      "requirements" to mapRequirements(requirements)
    )
    notificationClient.sendEmail(allocationTemplateId, allocatedOfficer.email!!, parameters, null)
  }

  private fun mapRequirements(requirements: List<ConvictionRequirement>): List<String> = requirements
    .map { requirement -> "${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length} ${requirement.lengthUnit}" }
}
