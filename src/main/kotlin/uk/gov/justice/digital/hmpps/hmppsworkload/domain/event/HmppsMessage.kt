package uk.gov.justice.digital.hmpps.hmppsworkload.domain.event

import java.time.LocalDateTime

data class HmppsMessage<T>(val eventType: String, val version: Int, val description: String, val detailUrl: String, val occurredAt: String, val additionalInformation: T, val personReference: PersonReference)

data class HmppsAuditMessage<T>(val operationId: String, val what: String = "CASE_ALLOCATED", val `when`: LocalDateTime = LocalDateTime.now(), val who: String, val service: String = "hmpps-workload", val details: T)

data class PersonReference(val identifiers: List<PersonReferenceType>)

data class PersonReferenceType(val type: String, val value: String)
