package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class CommunityCaseTypeRule : CaseTypeRule {

  private val communitySentenceCode = "SP"

  override fun isCaseType(sentenceTypeCode: String?, custodialStatusCode: String?): CaseType? =
    if (communitySentenceCode == sentenceTypeCode) CaseType.COMMUNITY else null
}
