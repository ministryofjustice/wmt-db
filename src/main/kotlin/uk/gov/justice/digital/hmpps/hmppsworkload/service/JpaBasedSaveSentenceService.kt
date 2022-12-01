package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import java.math.BigInteger
import java.time.ZoneId

@Service
class JpaBasedSaveSentenceService(
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val sentenceRepository: SentenceRepository
) : SaveSentenceService {

  override fun saveSentence(crn: String, sentenceId: BigInteger) {
    communityApiClient.getAllConvictions(crn)
      .filter { isCurrentSentence(it, sentenceId) }
      .map { conviction ->
        convictionToSentenceEntity(conviction!!, crn)
      }.blockFirst()?.let {
        sentenceRepository.save(it)
      } ?: sentenceRepository.findBySentenceId(sentenceId)?.let { sentenceRepository.delete(it) }
  }

  private fun isCurrentSentence(
    it: Conviction,
    sentenceId: BigInteger
  ) = it.sentence?.sentenceId == sentenceId && it.sentence.terminationDate == null

  private fun convictionToSentenceEntity(conviction: Conviction, crn: String): SentenceEntity = SentenceEntity(
    conviction.sentence!!.sentenceId,
    crn,
    conviction.sentence.startDate.atStartOfDay(ZoneId.systemDefault()),
    conviction.sentence.expectedSentenceEndDate?.atStartOfDay(ZoneId.systemDefault())
      ?: conviction.sentence.startDate.atStartOfDay(ZoneId.systemDefault()),
    conviction.sentence.sentenceType.code,
    conviction.custody?.keyDates?.expectedReleaseDate?.atStartOfDay(ZoneId.systemDefault())
  )
}
