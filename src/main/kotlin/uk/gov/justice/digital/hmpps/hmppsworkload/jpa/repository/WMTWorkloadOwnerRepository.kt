package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity

@Repository
interface WMTWorkloadOwnerRepository : CrudRepository<WMTWorkloadOwnerEntity, Long> {
  fun findFirstByOffenderManagerCodeAndTeamCodeOrderByIdDesc(offenderManagerCode: String, teamCode: String): WMTWorkloadOwnerEntity?
}
