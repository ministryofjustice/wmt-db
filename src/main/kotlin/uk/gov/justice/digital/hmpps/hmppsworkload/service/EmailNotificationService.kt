package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Offence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Sentence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper
import uk.gov.service.notify.NotificationClientApi
import java.math.BigInteger
import java.time.format.DateTimeFormatter

@Service
class EmailNotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
  private val communityApiClient: CommunityApiClient,
  private val hmppsTierApiClient: HmppsTierApiClient,
  private val gradeMapper: GradeMapper
) : NotificationService {

  override fun notifyAllocation(
    allocatedOfficer: Staff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    crn: String,
    convictionId: BigInteger,
    allocateCase: AllocateCase,
    allocatingOfficerUsername: String,
    teamCode: String
  ) {
    Mono.zip(communityApiClient.getConviction(crn, convictionId), hmppsTierApiClient.getTierByCrn(crn), communityApiClient.getStaffByUsername(allocatingOfficerUsername)).map { results ->
      val conviction = results.t1.orElseThrow()

      val parameters = mapOf(
        "case_name" to "${personSummary.firstName} ${personSummary.surname}",
        "crn" to crn,
        "officer_name" to "${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}",
        "court_name" to conviction.courtAppearance!!.courtName,
        "sentence_date" to conviction.courtAppearance.appearanceDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        // "induction_statement":
        "offences" to mapOffences(conviction.offences!!),
        "order" to mapOrder(conviction.sentence!!),
        "requirements" to mapRequirements(requirements),
        "tier" to results.t2,
        // "rosh" to
        // "rsrLevel" to
        // "rsrPercentage" to
        // "ogrsLevel" to
        // "ogrsPercentage" to
        // "previousConvictions" to
        "notes" to allocateCase.instructions,
        "allocatingOfficerName" to "${results.t3.staff.forenames} ${results.t3.staff.surname}",
        "allocatingOfficerGrade" to gradeMapper.deliusToStaffGrade(results.t3.staffGrade?.code),
        "allocatingOfficerTeam" to results.t3.teams?.find { team -> team.code == teamCode }?.description
      )
      notificationClient.sendEmail(allocationTemplateId, allocatedOfficer.email!!, parameters, null)
    }
  }

  private fun mapOrder(sentence: Sentence) = "${sentence.description} (${sentence.originalLength} ${sentence.originalLengthUnits})"

  private fun mapOffences(offences: List<Offence>): List<String> = offences
    .map { offence -> offence.detail.mainCategoryDescription }

  private fun mapRequirements(requirements: List<ConvictionRequirement>): List<String> = requirements
    .map { requirement -> "${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length} ${requirement.lengthUnit}" }
}
