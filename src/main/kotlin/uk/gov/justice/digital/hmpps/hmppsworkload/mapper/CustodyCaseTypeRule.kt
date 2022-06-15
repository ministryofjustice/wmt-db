package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class CustodyCaseTypeRule : CaseTypeRule {

  private val custodialStatusCodes = setOf("A", "C", "D", "R", "I", "AT")
  private val custodialSentenceCodes = setOf("SC", "NC")

  override fun isCaseType(sentenceTypeCode: String?, custodialStatusCode: String?): CaseType? =
    if (custodialSentenceCodes.contains(sentenceTypeCode) && custodialStatusCodes.contains(custodialStatusCode)) CaseType.CUSTODY else null
}
