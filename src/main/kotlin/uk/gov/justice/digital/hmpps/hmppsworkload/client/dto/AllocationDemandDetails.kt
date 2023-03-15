package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger
import java.time.LocalDate
import java.time.ZonedDateTime

data class AllocationDemandDetails @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val staff: StaffMember,
  val allocatingStaff: StaffMember,
  val initialAppointment: InitialAppointment?,
  val ogrs: RiskOGRS?,
  val sentence: SentenceDetails,
  val court: Court,
  val offences: List<OffenceDetails>,
  val activeRequirements: List<Requirement>,
)

data class RiskOGRS(
  val lastUpdatedDate: LocalDate,
  val score: Int,
) {
  fun getOgrsLevel(): String = when {
    score <= 49 -> "Low"
    score in 50..74 -> "Medium"
    score in 75..89 -> "High"
    else -> "Very High"
  }
}

data class SentenceDetails(
  val description: String,
  val date: ZonedDateTime,
  val length: String,
)

data class OffenceDetails(
  val mainCategory: String,
)

data class Court(
  val name: String,
  val appearanceDate: LocalDate,
)

data class Requirement(
  val mainCategory: String,
  val subCategory: String,
  val length: String,
  val id: BigInteger,
)
