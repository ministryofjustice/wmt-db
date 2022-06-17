package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Custody
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.CustodyStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Sentence
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.SentenceType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import java.math.BigInteger
import java.time.LocalDate

class CaseTypeMapperTest {

  @Test
  fun `using sentence end date to determine case type`() {

    val sentenceCommunity = Sentence(
      sentenceType = SentenceType(code = "SP", description = "COMMUNITY"),
      description = "communityDesc", startDate = LocalDate.now(), sentenceId = BigInteger.ONE,
      expectedSentenceEndDate = LocalDate.now().plusDays(20)
    )

    val sentenceLicense = Sentence(
      sentenceType = SentenceType(code = "SC", description = "LICENSE"),
      description = "communityDesc", startDate = LocalDate.now(), sentenceId = BigInteger.ONE,
      expectedSentenceEndDate = LocalDate.now().plusDays(15)
    )

    val convictionCommunity = Conviction(sentenceCommunity, active = true, convictionId = BigInteger.ONE)
    val convictionLicense = Conviction(
      sentenceLicense, custody = Custody(CustodyStatus("LicenseCode"), null),
      active = true, convictionId = BigInteger.TEN
    )

    val caseTypeActual = CaseTypeMapper(listOf(CustodyCaseTypeRule(), CommunityCaseTypeRule(), LicenseCaseTypeRule()))
      .getCaseType(listOf(convictionLicense, convictionCommunity))

    assertEquals(CaseType.COMMUNITY, caseTypeActual)
  }
}
