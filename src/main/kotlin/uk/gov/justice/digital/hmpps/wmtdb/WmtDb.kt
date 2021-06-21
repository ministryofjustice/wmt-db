package uk.gov.justice.digital.hmpps.wmtdb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class WmtDb

fun main(args: Array<String>) {
  runApplication<WmtDb>(*args)
}
