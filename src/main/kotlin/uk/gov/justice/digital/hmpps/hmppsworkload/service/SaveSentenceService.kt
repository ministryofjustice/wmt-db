package uk.gov.justice.digital.hmpps.hmppsworkload.service

import java.math.BigInteger

interface SaveSentenceService {

  fun saveSentence(crn: String, sentenceId: BigInteger)
}
