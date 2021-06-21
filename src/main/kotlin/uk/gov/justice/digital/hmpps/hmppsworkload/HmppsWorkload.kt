package uk.gov.justice.digital.hmpps.hmppsworkload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class HmppsWorkload

fun main(args: Array<String>) {
  runApplication<HmppsWorkload>(*args)
}
