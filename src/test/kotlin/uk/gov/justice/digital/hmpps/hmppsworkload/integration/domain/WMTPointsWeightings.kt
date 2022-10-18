package uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WorkloadPointsEntity

data class WMTPointsWeightings(val t2aWeightings: WorkloadPointsEntity, val normalWeightings: WorkloadPointsEntity)
