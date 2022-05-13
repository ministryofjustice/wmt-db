package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository

import org.springframework.data.repository.CrudRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import java.math.BigInteger

interface SentenceRepository : CrudRepository<SentenceEntity, Long> {

  fun findBySentenceId(sentenceId: BigInteger): SentenceEntity?
}
