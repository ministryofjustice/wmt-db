package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import java.math.BigInteger
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "SENTENCE")
data class SentenceEntity(

  @Id
  @Column
  val sentenceId: BigInteger,

  @Column
  val crn: String,

  @Column
  var startDate: ZonedDateTime,

  @Column
  var expectedEndDate: ZonedDateTime,

  @Column
  var sentenceTypeCode: String,

  @Column
  var expectedReleaseDate: ZonedDateTime?

)
