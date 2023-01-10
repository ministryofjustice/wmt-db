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
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Offence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenderAssessment
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.utils.DateUtils
import uk.gov.justice.digital.hmpps.hmppsworkload.utils.capitalize
import uk.gov.service.notify.NotificationClientApi
import uk.gov.service.notify.NotificationClientException
import uk.gov.service.notify.SendEmailResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

const val SCORE_UNAVAILABLE = "Score Unavailable"
private const val NOT_APPLICABLE = "N/A"

@Service
class NotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val assessRisksNeedsApiClient: AssessRisksNeedsApiClient,
  private val caseDetailsRepository: CaseDetailsRepository
) {

  fun notifyAllocation(allocatedOfficer: DeliusStaff, requirements: List<ConvictionRequirement>, allocateCase: AllocateCase, allocatingOfficerUsername: String, token: String): Mono<List<SendEmailResponse>> {
    return getNotifyData(allocateCase.crn, allocatingOfficerUsername, token, allocateCase.eventNumber).map { notifyData ->
      val caseDetails = caseDetailsRepository.findByIdOrNull(allocateCase.crn)!!
      val parameters = mapOf(
        "officer_name" to "${allocatedOfficer.staff.forenames} ${allocatedOfficer.staff.surname}",
        "induction_statement" to mapInductionAppointment(notifyData.initialAppointments, caseDetails.type, notifyData.conviction.sentence!!.startDate),
        "requirements" to mapRequirements(requirements),
      ).plus(getRiskParameters(notifyData.riskSummary, notifyData.riskPredictors, notifyData.assessment))
        .plus(getConvictionParameters(notifyData.conviction))
        .plus(getPersonOnProbationParameters(notifyData.personSummary, allocateCase))
        .plus(getLoggedInUserParameters(notifyData.allocatingDeliusStaff))
      val emailTo = HashSet(allocateCase.emailTo ?: emptySet())
      emailTo.add(allocatedOfficer.email!!)
      if (allocateCase.sendEmailCopyToAllocatingOfficer) emailTo.add(notifyData.allocatingDeliusStaff.email)
      emailTo.map { email -> addRecipientTo400Response(email) { notificationClient.sendEmail(allocationTemplateId, email, parameters, null) } }
    }
  }

  class NotificationInvalidSenderException(emailRecipient: String) : Exception("Unable to deliver to recipient $emailRecipient")

  private fun addRecipientTo400Response(emailRecipient: String, wrappedApiCall: () -> SendEmailResponse): SendEmailResponse {
    try {
      return wrappedApiCall.invoke()
    } catch (notificationException: NotificationClientException) {
      if (notificationException.httpResult == 400) {
        throw NotificationInvalidSenderException(emailRecipient)
      }
      throw notificationException
    }
  }

  private fun getLoggedInUserParameters(loggedInUser: DeliusStaff): Map<String, Any> = mapOf(
    "allocatingOfficerName" to "${loggedInUser.staff.forenames} ${loggedInUser.staff.surname}",
    "allocatingOfficerGrade" to loggedInUser.grade
  )

  private fun getPersonOnProbationParameters(personSummary: PersonSummary, allocateCase: AllocateCase): Map<String, Any> = mapOf(
    "case_name" to "${personSummary.firstName} ${personSummary.surname}",
    "crn" to allocateCase.crn,
    "notes" to allocateCase.instructions,
  )

  private fun getConvictionParameters(conviction: Conviction): Map<String, Any> = mapOf(
    "court_name" to conviction.courtAppearance!!.courtName,
    "sentence_date" to conviction.courtAppearance.appearanceDate.format(DateUtils.notifyDateFormat),
    "offences" to mapOffences(conviction.offences!!),
    "order" to conviction.sentence!!.mapOrder()
  )

  private fun getRiskParameters(riskSummary: RiskSummary?, riskPredictors: List<RiskPredictor>, assessment: OffenderAssessment?): Map<String, Any> {
    val latestRiskPredictor =
      riskPredictors.filter { riskPredictor -> riskPredictor.rsrScoreLevel != null && riskPredictor.rsrPercentageScore != null }
        .maxByOrNull { riskPredictor -> riskPredictor.completedDate ?: LocalDateTime.MIN }
    val rsrLevel = latestRiskPredictor?.rsrScoreLevel?.capitalize() ?: SCORE_UNAVAILABLE
    val rsrPercentage = latestRiskPredictor?.rsrPercentageScore?.toString() ?: NOT_APPLICABLE
    val rosh = riskSummary?.overallRiskLevel?.capitalize() ?: SCORE_UNAVAILABLE
    val ogrsLevel = assessment?.getOgrsLevel() ?: SCORE_UNAVAILABLE
    val ogrsPercentage = assessment?.ogrsScore?.toString() ?: NOT_APPLICABLE
    return mapOf(
      "rosh" to rosh,
      "rsrLevel" to rsrLevel,
      "rsrPercentage" to rsrPercentage,
      "ogrsLevel" to ogrsLevel,
      "ogrsPercentage" to ogrsPercentage,
    )
  }

  private fun mapInductionAppointment(appointments: List<Contact>, caseType: CaseType, sentenceStartDate: LocalDate): String {
    return when (caseType) {
      CaseType.CUSTODY -> "no initial appointment needed"
      else -> {
        val mostRecentAppointment = appointments.maxByOrNull { it.contactStart }
        if (mostRecentAppointment != null) {
          return if (ChronoUnit.DAYS.between(sentenceStartDate.atStartOfDay(ZoneId.systemDefault()), mostRecentAppointment.contactStart) >= 0) {
            "their initial appointment is scheduled for ${mostRecentAppointment.contactStart.format(DateUtils.notifyDateFormat)}"
          } else {
            "their initial appointment was scheduled for ${mostRecentAppointment.contactStart.format(DateUtils.notifyDateFormat)}"
          }
        }
        return "no date found for the initial appointment, please check with your team"
      }
    }
  }

  private fun mapOffences(offences: List<Offence>): List<String> = offences
    .map { offence -> offence.detail.mainCategoryDescription }

  private fun mapRequirements(requirements: List<ConvictionRequirement>): List<String> = requirements
    .map { requirement -> "${requirement.requirementTypeMainCategory.description}: ${requirement.requirementTypeSubCategory.description} ${requirement.length ?: ""} ${requirement.lengthUnit ?: ""}".trimEnd() }

  private fun getNotifyData(crn: String, allocatingOfficerUsername: String, token: String, eventNumber: Int): Mono<NotifyData> {
    val conviction = communityApiClient.getAllConvictions(crn).filter { it.eventNumber == eventNumber }.blockFirst()
    return Mono.zip(
      communityApiClient.getStaffByUsername(allocatingOfficerUsername),
      assessRisksNeedsApiClient.getRiskSummary(crn, token),
      assessRisksNeedsApiClient.getRiskPredictors(crn, token),
      communityApiClient.getAssessment(crn),
      communityApiClient.getSummaryByCrn(crn)
    )
      .flatMap { results ->
        communityApiClient.getInductionContacts(crn, conviction.sentence!!.startDate).map { initialAppointments ->
          NotifyData(conviction, initialAppointments, results.t1, results.t2.orElse(null), results.t3, results.t4.orElse(null), results.t5)
        }
      }
  }
}

data class NotifyData(
  val conviction: Conviction,
  val initialAppointments: List<Contact>,
  val allocatingDeliusStaff: DeliusStaff,
  val riskSummary: RiskSummary?,
  val riskPredictors: List<RiskPredictor>,
  val assessment: OffenderAssessment?,
  val personSummary: PersonSummary
)
