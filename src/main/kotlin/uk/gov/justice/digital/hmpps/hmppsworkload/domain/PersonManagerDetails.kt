package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.v3.oas.annotations.media.Schema
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.DeliusStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import java.time.ZonedDateTime
import java.util.UUID

data class PersonManagerDetails @JsonCreator constructor(
  @Schema(description = "Probation Practitioner ID")
  val id: UUID,
  @Schema(description = "Staff Code")
  val staffCode: String,
  @Schema(description = "Team Code")
  val teamCode: String,
  @Schema(description = "Created date")
  val createdDate: ZonedDateTime,
  @Schema(description = "crn")
  val crn: String,
  @Schema(description = "Staff Grade")
  val staffGrade: String,
  @Schema(description = "Staff Email")
  val staffEmail: String,
  @Schema(description = "Staff Forename")
  val staffForename: String,
  @Schema(description = "Staff Surname")
  val staffSurname: String
) {
  companion object {
    fun from(personManagerEntity: PersonManagerEntity, deliusStaff: DeliusStaff): PersonManagerDetails {
      return PersonManagerDetails(
        personManagerEntity.uuid,
        personManagerEntity.staffCode,
        personManagerEntity.teamCode,
        personManagerEntity.createdDate!!,
        personManagerEntity.crn,
        deliusStaff.grade,
        deliusStaff.email!!,
        deliusStaff.staff.forenames,
        deliusStaff.staff.surname
      )
    }
  }
}
