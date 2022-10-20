package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.ReductionEntity

data class OutOfDateReductions(val scheduledNowActive: List<ReductionEntity> = emptyList(), val activeNowArchived: List<ReductionEntity> = emptyList())
