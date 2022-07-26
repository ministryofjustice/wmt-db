package uk.gov.justice.digital.hmpps.hmppsworkload.domain

data class SaveResult<T>(val entity: T, val hasChanged: Boolean)
