package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import uk.gov.justice.digital.hmpps.hmppsworkload.client.AssessRisksNeedsApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.AllocationDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.InitialAppointment
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.OffenceDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Requirement
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskOGRS
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskPredictor
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.RiskSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffMember
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
import java.time.temporal.ChronoUnit

const val SCORE_UNAVAILABLE = "Score Unavailable"
private const val NOT_APPLICABLE = "N/A"

@Service
class NotificationService(
  private val notificationClient: NotificationClientApi,
  @Value("\${application.notify.allocation.template}") private val allocationTemplateId: String,
  private val assessRisksNeedsApiClient: AssessRisksNeedsApiClient,
  private val caseDetailsRepository: CaseDetailsRepository
) {

  fun notifyAllocation(allocationDetails: AllocationDetails, allocateCase: AllocateCase, token: String): Mono<List<SendEmailResponse>> {
    return getNotifyData(allocateCase.crn, token).map { notifyData ->
      val caseDetails = caseDetailsRepository.findByIdOrNull(allocateCase.crn)!!
      val parameters = mapOf(
        "officer_name" to "${allocationDetails.staff.name.getCombinedName()}",
        "induction_statement" to mapInductionAppointment(allocationDetails.initialAppointment, caseDetails.type),
        "requirements" to mapRequirements(allocationDetails.activeRequirements),
      ).plus(getRiskParameters(notifyData.riskSummary, notifyData.riskPredictors, allocationDetails.ogrs))
        .plus(getConvictionParameters(allocationDetails))
        .plus(getPersonOnProbationParameters(allocationDetails.name.getCombinedName(), allocateCase))
        .plus(getLoggedInUserParameters(allocationDetails.allocatingStaff))
      val emailTo = HashSet(allocateCase.emailTo ?: emptySet())
      emailTo.add(allocationDetails.staff.email!!)
      if (allocateCase.sendEmailCopyToAllocatingOfficer) emailTo.add(allocationDetails.allocatingStaff.email)
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

  private fun getLoggedInUserParameters(loggedInUser: StaffMember): Map<String, Any> = mapOf(
    "allocatingOfficerName" to "${loggedInUser.name.getCombinedName()}",
    "allocatingOfficerGrade" to loggedInUser.getGrade()
  )

  private fun getPersonOnProbationParameters(name: String, allocateCase: AllocateCase): Map<String, Any> = mapOf(
    "case_name" to name,
    "crn" to allocateCase.crn,
    "notes" to allocateCase.instructions,
  )

  private fun getConvictionParameters(allocationDetails: AllocationDetails): Map<String, Any> = mapOf(
    "court_name" to allocationDetails.court!!.name,
    "sentence_date" to allocationDetails.court.appearanceDate.format(DateUtils.notifyDateFormat),
    "offences" to mapOffences(allocationDetails.offences!!),
    "order" to "${allocationDetails.sentence.description} (${allocationDetails.sentence.length})"
  )

  private fun getRiskParameters(riskSummary: RiskSummary?, riskPredictors: List<RiskPredictor>, assessment: RiskOGRS?): Map<String, Any> {
    val latestRiskPredictor =
      riskPredictors.filter { riskPredictor -> riskPredictor.rsrScoreLevel != null && riskPredictor.rsrPercentageScore != null }
        .maxByOrNull { riskPredictor -> riskPredictor.completedDate ?: LocalDateTime.MIN }
    val rsrLevel = latestRiskPredictor?.rsrScoreLevel?.capitalize() ?: SCORE_UNAVAILABLE
    val rsrPercentage = latestRiskPredictor?.rsrPercentageScore?.toString() ?: NOT_APPLICABLE
    val rosh = riskSummary?.overallRiskLevel?.capitalize() ?: SCORE_UNAVAILABLE
    val ogrsLevel = assessment?.getOgrsLevel() ?: SCORE_UNAVAILABLE
    val ogrsPercentage = assessment?.score?.toString() ?: NOT_APPLICABLE
    return mapOf(
      "rosh" to rosh,
      "rsrLevel" to rsrLevel,
      "rsrPercentage" to rsrPercentage,
      "ogrsLevel" to ogrsLevel,
      "ogrsPercentage" to ogrsPercentage,
    )
  }

  private fun mapInductionAppointment(initialAppointment: InitialAppointment?, caseType: CaseType): String {
    return when (caseType) {
      CaseType.CUSTODY -> "no initial appointment needed"
      else -> {
        if (initialAppointment != null) {
          return if (ChronoUnit.DAYS.between(LocalDate.now(), initialAppointment.date) >= 0) {
            "their initial appointment is scheduled for ${initialAppointment.date.format(DateUtils.notifyDateFormat)}"
          } else {
            "their initial appointment was scheduled for ${initialAppointment.date.format(DateUtils.notifyDateFormat)}"
          }
        }
        return "no date found for the initial appointment, please check with your team"
      }
    }
  }

  private fun mapOffences(offences: List<OffenceDetails>): List<String> = offences
    .map { offence -> offence.mainCategory }

  private fun mapRequirements(requirements: List<Requirement>): List<String> = requirements
    .map { requirement -> "${requirement.mainCategory}: ${requirement.subCategory} ${requirement.length}".trimEnd() }

  private fun getNotifyData(crn: String, token: String): Mono<NotifyData> {
    return Mono.zip(
      assessRisksNeedsApiClient.getRiskSummary(crn, token),
      assessRisksNeedsApiClient.getRiskPredictors(crn, token)
    ).map { results ->
      NotifyData(results.t1.orElse(null), results.t2)
    }
  }
}

data class NotifyData(
  val riskSummary: RiskSummary?,
  val riskPredictors: List<RiskPredictor>
)
