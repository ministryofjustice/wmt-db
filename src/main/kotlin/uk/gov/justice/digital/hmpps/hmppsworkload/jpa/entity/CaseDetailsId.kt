package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import javax.persistence.Embeddable

@Embeddable
data class CaseDetailsId(
  val crn: String,
  var tier: Tier,
  var type: CaseType,
) : java.io.Serializable
