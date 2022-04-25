package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.AssessRisksNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Offence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Sentence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.CaseTypeMapper
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.DateMapper
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.GradeMapper
import uk.gov.service.notify.NotificationClientApi
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Service
class EmailNotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
  private val communityApiClient: CommunityApiClient,
  private val hmppsTierApiClient: HmppsTierApiClient,
  private val gradeMapper: GradeMapper,
  private val caseTypeMapper: CaseTypeMapper,
  private val dateMapper: DateMapper,
  private val assessRisksNeedsApiClient: AssessRisksNeedsApiClient
) : NotificationService {

  override fun notifyAllocation(
    allocatedOfficer: Staff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    crn: String,
    convictionId: BigInteger,
    allocateCase: AllocateCase,
    allocatingOfficerUsername: String,
    teamCode: String,
    token: String
  ) {
    val convictions = communityApiClient.getActiveConvictions(crn).block()!!
    val conviction = convictions.first { it.convictionId == convictionId }
    Mono.zip(communityApiClient.getInductionContacts(crn, conviction.sentence!!.startDate), hmppsTierApiClient.getTierByCrn(crn), communityApiClient.getStaffByUsername(allocatingOfficerUsername), assessRisksNeedsApiClient.getRiskSummary(crn, token)).map { results ->

      val parameters = mapOf(
        "case_name" to "${personSummary.firstName} ${personSummary.surname}",
        "crn" to crn,
        "officer_name" to "${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}",
        "court_name" to conviction.courtAppearance!!.courtName,
        "sentence_date" to conviction.courtAppearance.appearanceDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        "induction_statement" to mapInductionAppointment(results.t1, caseTypeMapper.getCaseType(convictions, convictionId), conviction.sentence.startDate),
        "offences" to mapOffences(conviction.offences!!),
        "order" to mapOrder(conviction.sentence),
        "requirements" to mapRequirements(requirements),
        "tier" to results.t2,
        "rosh" to results.t4.map { riskSummary ->
          capitalize(riskSummary.overallRiskLevel)
        }.orElse(null),
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

  private fun mapInductionAppointment(appointments: List<Contact>, caseType: CaseType, sentenceStartDate: LocalDate): String {
    return when (caseType) {
      CaseType.CUSTODY -> "No induction appointment is needed"
      else -> {
        val mostRecentAppointment = appointments.maxByOrNull { it.contactStart }
        if (mostRecentAppointment != null) {
          return if (ChronoUnit.DAYS.between(mostRecentAppointment.contactStart, sentenceStartDate) >= 0) {
            "Their induction has been booked and is due on ${mostRecentAppointment.contactStart.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
          } else {
            "Their induction is overdue and was due on ${mostRecentAppointment.contactStart.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
          }
        }
        val inductionDueDate = dateMapper.addBusinessDays(sentenceStartDate, 5L)
        return "Their induction has not been booked and is due on ${inductionDueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}"
      }
    }
  }

  private fun capitalize(value: String?): String? = value?.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
      Locale.getDefault()
    ) else it.toString()
  }

  private fun mapOrder(sentence: Sentence) = "${sentence.description} (${sentence.originalLength} ${sentence.originalLengthUnits})"

  private fun mapOffences(offences: List<Offence>): List<String> = offences
    .map { offence -> offence.detail.mainCategoryDescription }

  private fun mapRequirements(requirements: List<ConvictionRequirement>): List<String> = requirements
    .map { requirement -> "${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length} ${requirement.lengthUnit}" }
}
