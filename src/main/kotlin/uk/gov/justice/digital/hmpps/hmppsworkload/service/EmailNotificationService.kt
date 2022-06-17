package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.AssessRisksNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
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
import uk.gov.service.notify.SendEmailResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Optional

@Service
class EmailNotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  @Qualifier("hmppsTierApiClient") private val hmppsTierApiClient: HmppsTierApiClient,
  private val gradeMapper: GradeMapper,
  private val caseTypeMapper: CaseTypeMapper,
  private val dateMapper: DateMapper,
  private val assessRisksNeedsApiClient: AssessRisksNeedsApiClient
) : NotificationService {

  override fun notifyAllocation(
    allocatedOfficer: Staff,
    personSummary: PersonSummary,
    requirements: List<ConvictionRequirement>,
    allocateCase: AllocateCase,
    allocatingOfficerUsername: String,
    teamCode: String,
    token: String
  ): Mono<List<SendEmailResponse>> {
    return communityApiClient.getAllConvictions(allocateCase.crn).map { convictions ->
      convictions.groupBy { it.active }
    }.flatMap { convictions ->
      val activeConvictions = convictions.getOrDefault(true, emptyList())
      val previousConvictions = Optional.ofNullable(convictions[false])
      val conviction = activeConvictions.first { it.convictionId == allocateCase.eventId }
      Mono.zip(
        communityApiClient.getInductionContacts(allocateCase.crn, conviction.sentence!!.startDate),
        hmppsTierApiClient.getTierByCrn(allocateCase.crn),
        communityApiClient.getStaffByUsername(allocatingOfficerUsername),
        assessRisksNeedsApiClient.getRiskSummary(allocateCase.crn, token),
        assessRisksNeedsApiClient.getRiskPredictors(allocateCase.crn, token),
        communityApiClient.getAssessment(allocateCase.crn)
      ).map { results ->
        val latestRiskPredictor = Optional.ofNullable(
          results.t5.filter { riskPredictor -> riskPredictor.rsrScoreLevel != null && riskPredictor.rsrPercentageScore != null }
            .maxByOrNull { riskPredictor -> riskPredictor.completedDate ?: LocalDateTime.MIN }
        )
        val parameters = mapOf(
          "case_name" to "${personSummary.firstName} ${personSummary.surname}",
          "crn" to allocateCase.crn,
          "officer_name" to "${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}",
          "court_name" to conviction.courtAppearance!!.courtName,
          "sentence_date" to conviction.courtAppearance.appearanceDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
          "induction_statement" to mapInductionAppointment(results.t1, caseTypeMapper.getCaseType(activeConvictions, allocateCase.eventId), conviction.sentence.startDate),
          "offences" to mapOffences(conviction.offences!!),
          "order" to mapOrder(conviction.sentence),
          "requirements" to mapRequirements(requirements),
          "tier" to results.t2,
          "rosh" to results.t4.map { riskSummary ->
            capitalize(riskSummary.overallRiskLevel)
          }.orElse("Score Unavailable"),
          "rsrLevel" to latestRiskPredictor.map { riskPredictor -> capitalize(riskPredictor.rsrScoreLevel) }.orElse("Score Unavailable"),
          "rsrPercentage" to latestRiskPredictor.map { riskPredictor -> riskPredictor.rsrPercentageScore?.toString() }.orElse("N/A"),
          "ogrsLevel" to results.t6.map { assessment -> assessment.ogrsScore?.let { orgsScoreToLevel(it.toInt()) } }.orElse("Score Unavailable"),
          "ogrsPercentage" to results.t6.map { assessment -> assessment.ogrsScore?.toString() }.orElse("N/A"),
          "previousConvictions" to previousConvictions.map { mapConvictionsToOffenceDescription(it) }.orElse(listOf("N/A")),
          "notes" to allocateCase.instructions,
          "allocatingOfficerName" to "${results.t3.staff.forenames} ${results.t3.staff.surname}",
          "allocatingOfficerGrade" to gradeMapper.deliusToStaffGrade(results.t3.staffGrade?.code),
          "allocatingOfficerTeam" to results.t3.teams?.find { team -> team.code == teamCode }?.description
        )
        val emailTo = HashSet(allocateCase.emailTo ?: emptySet())
        emailTo.add(allocatedOfficer.email!!)
        emailTo.map { email -> notificationClient.sendEmail(allocationTemplateId, email, parameters, null) }
      }
    }
  }

  private fun mapConvictionsToOffenceDescription(convictions: List<Conviction>): List<String> = convictions
    .filter { it.offences != null }
    .flatMap { it.offences!! }
    .map { offence -> offence.detail.description }

  private fun orgsScoreToLevel(ogrsScore: Int): String? = when {
    ogrsScore <= 49 -> "Low"
    ogrsScore in 50..74 -> "Medium"
    ogrsScore in 75..89 -> "High"
    ogrsScore >= 90 -> "Very High"
    else -> null
  }

  private fun mapInductionAppointment(appointments: List<Contact>, caseType: CaseType, sentenceStartDate: LocalDate): String {
    return when (caseType) {
      CaseType.CUSTODY -> "No induction appointment is needed"
      else -> {
        val mostRecentAppointment = appointments.maxByOrNull { it.contactStart }
        if (mostRecentAppointment != null) {
          return if (ChronoUnit.DAYS.between(sentenceStartDate.atStartOfDay(ZoneId.systemDefault()), mostRecentAppointment.contactStart) >= 0) {
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

  private fun capitalize(value: String?): String? = value?.let {
    if (it.isNotEmpty() && it.isNotBlank()) {
      return it[0].uppercase() + it.substring(1).lowercase()
    }
    return it
  }

  private fun mapOrder(sentence: Sentence) = "${sentence.description} (${sentence.originalLength} ${sentence.originalLengthUnits})"

  private fun mapOffences(offences: List<Offence>): List<String> = offences
    .map { offence -> offence.detail.mainCategoryDescription }

  private fun mapRequirements(requirements: List<ConvictionRequirement>): List<String> = requirements
    .map { requirement -> "${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length} ${requirement.lengthUnit}" }
}
