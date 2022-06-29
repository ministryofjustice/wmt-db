package uk.gov.justice.digital.hmpps.hmppsworkload.integration.populateRealtime

import com.opencsv.CSVWriter
import com.opencsv.bean.StatefulBeanToCsv
import com.opencsv.bean.StatefulBeanToCsvBuilder
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.reactive.function.BodyInserters
import uk.gov.justice.digital.hmpps.hmppsworkload.controller.CaseCsv
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import java.io.File
import java.io.FileWriter

class PopulateOffenderEvents : IntegrationTestBase() {

  @Test
  fun `populate offender events from csv upload of crns`() {
    val crn = "CRN1"

    singleActiveConvictionResponse(crn)
    singleActiveConvictionResponseForAllConvictions(crn)
    singleActiveConvictionResponse(crn)
    tierCalculationResponse(crn)
    val cases = listOf(CaseCsv(crn))
    val csvFile = generateCsv(cases)
    val multipartBodyBuilder = MultipartBodyBuilder()
    multipartBodyBuilder.part("file", FileSystemResource(csvFile))

    webTestClient.post()
      .uri("/cases/upload")
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
      .exchange()
      .expectStatus()
      .isOk

    await untilCallTo {
      caseDetailsRepository.count()
    } matches { it!! > 0 }

    val caseDetail = caseDetailsRepository.findAll().first()
    Assertions.assertEquals(crn, caseDetail.crn)
    Assertions.assertEquals(CaseType.CUSTODY, caseDetail.type)
    Assertions.assertEquals(Tier.B3, caseDetail.tier)
  }

  fun generateCsv(unallocatedCases: List<CaseCsv>): File {
    val tempFile = kotlin.io.path.createTempFile().toFile()
    val writer = FileWriter(tempFile)

    val sbc: StatefulBeanToCsv<CaseCsv> = StatefulBeanToCsvBuilder<CaseCsv>(writer)
      .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
      .build()
    sbc.write(unallocatedCases)
    writer.close()
    return tempFile
  }
}
