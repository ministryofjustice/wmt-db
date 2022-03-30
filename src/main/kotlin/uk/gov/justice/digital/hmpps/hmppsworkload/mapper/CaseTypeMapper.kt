package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class CaseTypeMapper(
  private val caseTypeRules: List<CaseTypeRule>
) {
  fun getCaseType(activeConvictions: List<Conviction>, convictionId: Long): CaseType {
    val allConvictionTypes = activeConvictions.mapNotNull { conviction ->
      convictionToCaseType(conviction)?.let { caseType ->
        {
          conviction.convictionId to caseType
        }
      }
    }.associate { it.invoke() }
    var caseType = allConvictionTypes.getValue(convictionId)
    if (caseType == CaseType.LICENSE && allConvictionTypes.containsValue(CaseType.CUSTODY)) {
      caseType = CaseType.CUSTODY
    }
    return caseType
  }

  fun convictionToCaseType(conviction: Conviction): CaseType? {
    var caseType: CaseType? = null
    for (caseTypeRule in caseTypeRules) {
      if (caseTypeRule.isCaseType(conviction.sentence!!.sentenceType.code, conviction.custody?.status?.code)) {
        caseType = caseTypeRule.getCaseType()
        break
      }
    }
    return caseType
  }
}
