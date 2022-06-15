package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class CommunityCaseTypeRule : CaseTypeRule {

  private val communitySentenceCodes = setOf("SP", "NP")

  override fun isCaseType(sentenceTypeCode: String?, custodialStatusCode: String?): Boolean = communitySentenceCodes.contains(sentenceTypeCode)

  override fun getCaseType(): CaseType = CaseType.COMMUNITY
}
