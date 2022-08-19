package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import javax.transaction.Transactional

@Service
class UpdateWorkloadService(
  private val personManagerRepository: PersonManagerRepository,
  private val eventManagerRepository: EventManagerRepository,
  private val requirementManagerRepository: RequirementManagerRepository
) {

  @Transactional
  fun setWorkloadInactive(crn: String) {
    personManagerRepository.setInactiveTrueFor(crn)
    eventManagerRepository.setInactiveTrueFor(crn)
    requirementManagerRepository.setInactiveTrueFor(crn)
  }
}
