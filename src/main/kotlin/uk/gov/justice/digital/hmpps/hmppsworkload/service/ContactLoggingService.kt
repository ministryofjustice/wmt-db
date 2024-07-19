package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.apache.logging.log4j.LoggingException
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.ContactLoggingMessage

private const val CRN = "CRN"
private const val EDIT_NOTES_SCREEN_ACCESSED = "EDIT_NOTES_SCREEN_ACCESSED"
private const val NOTES_EDITED = "NOTES_EDITED"
private const val TEAM_CODE = "TEAM_CODE"

@Service
class ContactLoggingService {
  val log = LoggerFactory.getLogger(this::class.java)
  suspend fun logContact(message: ContactLoggingMessage): Boolean {
    try {
      MDC.put(CRN, message.crn)
      MDC.put(TEAM_CODE, message.teamCode)
      MDC.put(EDIT_NOTES_SCREEN_ACCESSED, message.editNotesScreenAccessed.toString())
      MDC.put(NOTES_EDITED, message.notesEdited.toString())
      log.info("CRN: ${message.crn} allocated, EDIT_NOTES_SCREEN_ACCESSED: ${message.editNotesScreenAccessed}, NOTES_EDITED: ${message.notesEdited}")
      MDC.remove(CRN)
      MDC.remove(TEAM_CODE)
      MDC.remove(EDIT_NOTES_SCREEN_ACCESSED)
      MDC.remove(NOTES_EDITED)
      return true
    } catch (e: LoggingException) {
      log.error(e.message, e)
      return false
    }
  }
}
