package uk.gov.justice.digital.hmpps.hmppsworkload.utils

import org.springframework.stereotype.Component

@Component
object UserContext {
  var authToken = ThreadLocal<String>()

  fun setAuthToken(aToken: String) {
    authToken.set(aToken)
  }

  fun getAuthToken(): String {
    return authToken.get()
  }
}
