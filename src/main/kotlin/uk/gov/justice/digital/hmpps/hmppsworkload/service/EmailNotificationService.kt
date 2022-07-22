package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.AssessRisksNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Contact
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ConvictionRequirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Offence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderAssessment
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.DateMapper
import uk.gov.justice.digital.hmpps.hmppsworkload.utils.capitalize
import uk.gov.service.notify.NotificationClientApi
import uk.gov.service.notify.SendEmailResponse
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

const val SCORE_UNAVAILABLE = "Score Unavailable"
private const val NOT_APPLICABLE = "N/A"

@Service
class EmailNotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val dateMapper: DateMapper,
  private val assessRisksNeedsApiClient: AssessRisksNeedsApiClient,
  private val caseDetailsRepository: CaseDetailsRepository
) : NotificationService {

  override fun notifyAllocation(allocatedOfficer: Staff, personSummary: PersonSummary, requirements: List<ConvictionRequirement>, allocateCase: AllocateCase, allocatingOfficerUsername: String, token: String): Mono<List<SendEmailResponse>> {
    return getNotifyData(allocateCase.crn, allocatingOfficerUsername, token, allocateCase.eventId).map { notifyData ->
      val caseDetails = caseDetailsRepository.findByIdOrNull(allocateCase.crn)!!
      val parameters = mapOf(
        "officer_name" to "${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}",
        "induction_statement" to mapInductionAppointment(notifyData.initialAppointments, caseDetails.type, notifyData.conviction.sentence!!.startDate),
        "requirements" to mapRequirements(requirements),
      ).plus(getRiskParameters(notifyData.riskSummary, notifyData.riskPredictors, notifyData.assessment, caseDetails.tier))
        .plus(getConvictionParameters(notifyData.conviction, notifyData.previousConvictions))
        .plus(getPersonOnProbationParameters(personSummary, allocateCase))
        .plus(getLoggedInUserParameters(notifyData.allocatingStaff))
      val emailTo = HashSet(allocateCase.emailTo ?: emptySet())
      emailTo.add(allocatedOfficer.email)
      emailTo.map { email -> notificationClient.sendEmail(allocationTemplateId, email, parameters, null) }
    }
  }

  private fun getLoggedInUserParameters(loggedInUser: Staff): Map<String, Any> = mapOf(
    "allocatingOfficerName" to "${loggedInUser.staff.forenames} ${loggedInUser.staff.surname}",
    "allocatingOfficerGrade" to loggedInUser.grade
  )

  private fun getPersonOnProbationParameters(personSummary: PersonSummary, allocateCase: AllocateCase): Map<String, Any> = mapOf(
    "case_name" to "${personSummary.firstName} ${personSummary.surname}",
    "crn" to allocateCase.crn,
    "notes" to allocateCase.instructions,
  )

  private fun getConvictionParameters(conviction: Conviction, previousConvictions: List<Conviction>): Map<String, Any> = mapOf(
    "court_name" to conviction.courtAppearance!!.courtName,
    "sentence_date" to conviction.courtAppearance.appearanceDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
    "offences" to mapOffences(conviction.offences!!),
    "order" to conviction.sentence!!.mapOrder(),
    "previousConvictions" to mapConvictionsToOffenceDescription(previousConvictions)
  )

  private fun getRiskParameters(riskSummary: RiskSummary?, riskPredictors: List<RiskPredictor>, assessment: OffenderAssessment?, tier: Tier): Map<String, Any> {
    val latestRiskPredictor =
      riskPredictors.filter { riskPredictor -> riskPredictor.rsrScoreLevel != null && riskPredictor.rsrPercentageScore != null }
        .maxByOrNull { riskPredictor -> riskPredictor.completedDate ?: LocalDateTime.MIN }
    val rsrLevel = latestRiskPredictor?.rsrScoreLevel?.capitalize() ?: SCORE_UNAVAILABLE
    val rsrPercentage = latestRiskPredictor?.rsrPercentageScore?.toString() ?: NOT_APPLICABLE
    val rosh = riskSummary?.overallRiskLevel?.capitalize() ?: SCORE_UNAVAILABLE
    val ogrsLevel = assessment?.getOgrsLevel() ?: SCORE_UNAVAILABLE
    val ogrsPercentage = assessment?.ogrsScore?.toString() ?: NOT_APPLICABLE
    return mapOf(
      "tier" to tier,
      "rosh" to rosh,
      "rsrLevel" to rsrLevel,
      "rsrPercentage" to rsrPercentage,
      "ogrsLevel" to ogrsLevel,
      "ogrsPercentage" to ogrsPercentage,
    )
  }

  private fun mapConvictionsToOffenceDescription(convictions: List<Conviction>): List<String> {
    val mappedConvictions = convictions
      .filter { it.offences != null }
      .flatMap { it.offences!! }
      .map { offence -> offence.detail.description }
    if (mappedConvictions.isEmpty()) {
      return listOf(NOT_APPLICABLE)
    }
    return mappedConvictions
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

  private fun mapOffences(offences: List<Offence>): List<String> = offences
    .map { offence -> offence.detail.mainCategoryDescription }

  private fun mapRequirements(requirements: List<ConvictionRequirement>): List<String> = requirements
    .map { requirement -> "${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length ?: ""} ${requirement.lengthUnit ?: ""}".trimEnd() }

  private fun getNotifyData(crn: String, allocatingOfficerUsername: String, token: String, eventId: BigInteger): Mono<NotifyData> = Mono.zip(
    communityApiClient.getAllConvictions(crn), communityApiClient.getStaffByUsername(allocatingOfficerUsername), assessRisksNeedsApiClient.getRiskSummary(crn, token),
    assessRisksNeedsApiClient.getRiskPredictors(crn, token),
    communityApiClient.getAssessment(crn),
  )
    .flatMap { results ->
      val conviction = results.t1.first { it.convictionId == eventId }
      communityApiClient.getInductionContacts(crn, conviction.sentence!!.startDate).map { initialAppointments ->
        NotifyData(conviction, results.t1.filter { !it.active }, initialAppointments, results.t2, results.t3.orElse(null), results.t4, results.t5.orElse(null))
      }
    }
}

data class NotifyData(
  val conviction: Conviction,
  val previousConvictions: List<Conviction>,
  val initialAppointments: List<Contact>,
  val allocatingStaff: Staff,
  val riskSummary: RiskSummary?,
  val riskPredictors: List<RiskPredictor>,
  val assessment: OffenderAssessment?
)
