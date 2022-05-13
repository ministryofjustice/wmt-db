package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
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
    val sentence = communityApiClient.getAllConvictions(crn).map { convictions ->
      convictions.first { conviction -> conviction.sentence?.sentenceId == sentenceId }.sentence
    }.block()!!

    val sentenceToSave = sentenceRepository.findBySentenceId(sentenceId) ?: SentenceEntity(
      null,
      sentence.sentenceId,
      crn,
      sentence.startDate.atStartOfDay(ZoneId.systemDefault()),
      sentence.expectedSentenceEndDate.atStartOfDay(ZoneId.systemDefault()),
      sentence.terminationDate?.atStartOfDay(ZoneId.systemDefault()),
      sentence.sentenceType.code
    )

    sentenceToSave.startDate = sentence.startDate.atStartOfDay(ZoneId.systemDefault())
    sentenceToSave.expectedEndDate = sentence.expectedSentenceEndDate.atStartOfDay(ZoneId.systemDefault())
    sentenceToSave.terminatedDate = sentence.terminationDate?.atStartOfDay(ZoneId.systemDefault())
    sentenceToSave.sentenceTypeCode = sentence.sentenceType.code

    sentenceRepository.save(sentenceToSave)
  }
}
