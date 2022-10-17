package uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain

import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WMTWorkloadEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WorkloadPointsCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.OffenderManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.TeamEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity

data class WMTStaff(
  val offenderManager: OffenderManagerEntity,
  val team: TeamEntity,
  val wmtWorkloadOwnerEntity: WMTWorkloadOwnerEntity,
  val workload: WMTWorkloadEntity,
  val workloadPointsCalculation: WorkloadPointsCalculationEntity
)
