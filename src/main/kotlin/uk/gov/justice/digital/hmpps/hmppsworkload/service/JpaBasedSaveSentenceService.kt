package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.SentenceEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import java.math.BigInteger
import java.time.ZoneId
import java.util.Optional

@Service
class JpaBasedSaveSentenceService(
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val sentenceRepository: SentenceRepository
) : SaveSentenceService {

  override fun saveSentence(crn: String, sentenceId: BigInteger) {
    communityApiClient.getAllConvictions(crn)
      .map { convictions ->
        Optional.ofNullable(convictions.firstOrNull { it.sentence?.sentenceId == sentenceId && it.sentence.terminationDate == null })
      }.block()!!.ifPresent { conviction ->
      val sentenceToSave = sentenceRepository.findBySentenceId(sentenceId) ?: SentenceEntity(
        conviction.sentence!!.sentenceId,
        crn,
        conviction.sentence.startDate.atStartOfDay(ZoneId.systemDefault()),
        conviction.sentence.expectedSentenceEndDate?.atStartOfDay(ZoneId.systemDefault()) ?: conviction.sentence.startDate.atStartOfDay(ZoneId.systemDefault()),
        conviction.sentence.sentenceType.code,
        conviction.custody?.keyDates?.expectedReleaseDate?.atStartOfDay(ZoneId.systemDefault())
      )
      sentenceToSave.startDate = conviction.sentence!!.startDate.atStartOfDay(ZoneId.systemDefault())
      sentenceToSave.expectedEndDate = conviction.sentence.expectedSentenceEndDate?.atStartOfDay(ZoneId.systemDefault()) ?: conviction.sentence.startDate.atStartOfDay(ZoneId.systemDefault())
      sentenceToSave.sentenceTypeCode = conviction.sentence.sentenceType.code
      sentenceToSave.expectedReleaseDate = conviction.custody?.keyDates?.expectedReleaseDate?.atStartOfDay(ZoneId.systemDefault())

      sentenceRepository.save(sentenceToSave)
    }
  }
}
