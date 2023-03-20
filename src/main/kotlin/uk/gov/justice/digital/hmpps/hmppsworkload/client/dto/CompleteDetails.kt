package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDate

data class CompleteDetails @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val type: String,
  val initialAppointment: InitialAppointment?,
  val staff: StaffMember?,
)

data class InitialAppointment(val date: LocalDate)
