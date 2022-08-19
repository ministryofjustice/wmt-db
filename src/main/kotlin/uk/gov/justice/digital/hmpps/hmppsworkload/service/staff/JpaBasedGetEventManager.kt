package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.EventCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import java.util.UUID

@Service
class JpaBasedGetEventManager(private val eventManagerRepository: EventManagerRepository, private val caseDetailsRepository: CaseDetailsRepository) : GetEventManager {
  override fun findById(id: UUID): EventManagerEntity? = eventManagerRepository.findByUuid(id)
  override fun findLatestByStaffAndTeam(staffCode: String, teamCode: String): EventCase? = eventManagerRepository.findByStaffCodeAndTeamCodeAndIsActiveTrue(staffCode, teamCode)
    .filter { caseDetailsRepository.existsById(it.crn) }
    .maxByOrNull { it.createdDate!! }?.let { eventManagerEntity ->
      caseDetailsRepository.findByIdOrNull(eventManagerEntity.crn)?.let { caseDetails ->
        EventCase(caseDetails.tier, caseDetails.type, caseDetails.crn, eventManagerEntity.createdDate!!)
      }
    }
}
