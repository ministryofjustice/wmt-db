package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import java.util.UUID

@Service
class JpaBasedGetPersonManager(private val personManagerRepository: PersonManagerRepository) : GetPersonManager {
  override fun findById(id: UUID): PersonManagerEntity? = personManagerRepository.findByUuid(id)
}
