package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import java.math.BigInteger
import java.time.ZonedDateTime

interface SentenceRepository : CrudRepository<SentenceEntity, BigInteger> {

  fun findBySentenceId(sentenceId: BigInteger): SentenceEntity?

  fun findByCrnInAndExpectedEndDateGreaterThanEqual(crns: List<String>, endDateAfter: ZonedDateTime): List<SentenceEntity>

  fun findByCrnInAndExpectedReleaseDateGreaterThanEqual(crns: List<String>, endDateAfter: ZonedDateTime): List<SentenceEntity>
}
