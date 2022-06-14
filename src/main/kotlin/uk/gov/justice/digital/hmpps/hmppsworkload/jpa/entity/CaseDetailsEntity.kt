package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "CASE_DETAILS")
@EntityListeners(AuditingEntityListener::class)
@IdClass(CaseDetailsId::class)
data class CaseDetailsEntity(

  @Id
  @Column
  val crn: String,

  @Id
  @Column
  @Enumerated(EnumType.STRING)
  var tier: Tier,

  @Id
  @Column
  @Enumerated(EnumType.STRING)
  var type: CaseType,

)
