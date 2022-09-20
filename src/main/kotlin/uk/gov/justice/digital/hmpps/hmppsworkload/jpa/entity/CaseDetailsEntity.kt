package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "CASE_DETAILS")
data class CaseDetailsEntity(

  @Id
  @Column
  val crn: String,

  @Column
  @Enumerated(EnumType.STRING)
  var tier: Tier,

  @Column
  @Enumerated(EnumType.STRING)
  var type: CaseType,

  @Column
  var firstName: String,

  @Column
  var surname: String,

)
