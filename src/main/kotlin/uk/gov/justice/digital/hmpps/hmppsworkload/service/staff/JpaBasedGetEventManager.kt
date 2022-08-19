package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.EventDetails
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import java.util.UUID

@Service
class JpaBasedGetEventManager(private val eventManagerRepository: EventManagerRepository, private val caseDetailsRepository: CaseDetailsRepository) {
  fun findById(id: UUID): EventManagerEntity? = eventManagerRepository.findByUuid(id)
  fun findLatestByStaffAndTeam(staffCode: String, teamCode: String): EventDetails? =
    eventManagerRepository.findFirstByStaffCodeAndTeamCodeAndIsActiveTrueOrderByCreatedDateDesc(staffCode, teamCode)?.let { eventManagerEntity ->
      caseDetailsRepository.findByIdOrNull(eventManagerEntity.crn)?.let { caseDetails ->
        EventDetails(caseDetails.tier, caseDetails.type, caseDetails.crn, eventManagerEntity.createdDate!!)
      }
    }
}
