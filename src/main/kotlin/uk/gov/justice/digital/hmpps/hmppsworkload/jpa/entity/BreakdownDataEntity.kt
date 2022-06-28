package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity
import java.io.Serializable

data class BreakdownDataEntity(
  val standardDeliveryReportCount: Int,
  val fastDeliveryReportCount: Int,
  val paroleReportsCount: Int,
  val communityCaseAssessmentCount: Int,
  val licenseCaseAssessmentCount: Int,
  val contactsPerformedOutsideCaseloadCount: Map<String, Int>,
  val contactsPerformedByOthersCount: Map<String, Int>,
  val contactTypeWeightings: Map<String, Int>
) : Serializable
