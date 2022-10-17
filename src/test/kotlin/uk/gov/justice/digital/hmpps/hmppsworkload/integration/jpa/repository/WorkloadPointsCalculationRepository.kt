package uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WorkloadPointsCalculationEntity

interface WorkloadPointsCalculationRepository : CrudRepository<WorkloadPointsCalculationEntity, Long>
