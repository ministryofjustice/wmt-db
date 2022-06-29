package uk.gov.justice.digital.hmpps.hmppsworkload.controller

import com.opencsv.bean.CsvBindByPosition
import com.opencsv.bean.CsvToBeanBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import uk.gov.justice.digital.hmpps.hmppsworkload.service.PopulateRealtimeService
import java.io.InputStreamReader

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class PopulateRealtimeController(private val populateRealtimeService: PopulateRealtimeService) {

  @PostMapping("/cases/upload")
  fun uploadCases(@RequestParam("file") file: MultipartFile): ResponseEntity<Void> {
    populateRealtimeService.sendEvents(fileToCases(file))
    return ResponseEntity.ok().build()
  }

  @Throws(Exception::class)
  fun fileToCases(file: MultipartFile): List<CaseCsv> {
    val reader = InputStreamReader(file.inputStream)
    val cb = CsvToBeanBuilder<CaseCsv>(reader)
      .withType(CaseCsv::class.java)
      .build()
    val unallocatedCases = cb.parse()
    reader.close()
    return unallocatedCases
  }
}

data class CaseCsv(
  @CsvBindByPosition(position = 0)
  var crn: String? = null
)
