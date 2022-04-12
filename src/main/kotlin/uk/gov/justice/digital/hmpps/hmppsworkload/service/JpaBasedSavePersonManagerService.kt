package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PersonSummary
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.AllocateCase
import uk.gov.justice.digital.hmpps.hmppsworkload.error.PersonManagerAlreadyAllocatedError
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository

@Service
class JpaBasedSavePersonManagerService(
  private val personManagerRepository: PersonManagerRepository,
  private val telemetryService: TelemetryService,
  private val successUpdater: SuccessUpdater
) : SavePersonManagerService {
  override fun savePersonManager(
    teamCode: String,
    staff: Staff,
    allocateCase: AllocateCase,
    loggedInUser: String,
    personSummary: PersonSummary
  ): PersonManagerEntity =
    personManagerRepository.findFirstByCrnOrderByCreatedDateDesc(allocateCase.crn)?.let { personManager ->
      if (personManager.staffId == staff.staffIdentifier && personManager.teamCode == teamCode) {
        return personManager
      }
      throw PersonManagerAlreadyAllocatedError("CRN ${allocateCase.crn} already allocated")
    } ?: run {
      val personManagerEntity = PersonManagerEntity(crn = allocateCase.crn, staffId = staff.staffIdentifier, staffCode = staff.staffCode, teamCode = teamCode, offenderName = "${personSummary.firstName} ${personSummary.surname}", createdBy = loggedInUser, providerCode = staff.probationArea!!.code)
      personManagerRepository.save(personManagerEntity)
      telemetryService.trackPersonManagerAllocated(personManagerEntity)
      successUpdater.updatePerson(personManagerEntity.crn, personManagerEntity.uuid, personManagerEntity.createdDate!!)
      personManagerEntity
    }
}
