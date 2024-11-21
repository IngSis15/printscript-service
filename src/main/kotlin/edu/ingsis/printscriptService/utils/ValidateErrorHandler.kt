package edu.ingsis.printscriptService.utils

import edu.ingsis.printscriptService.dto.ErrorDTO
import org.slf4j.LoggerFactory
import runner.ErrorHandler

class ValidateErrorHandler : ErrorHandler {
    private val errors = mutableListOf<ErrorDTO>()
    private val logger = LoggerFactory.getLogger(ValidateErrorHandler::class.java)

    override fun handleError(message: String) {
        logger.error("Error encountered: $message")
        errors.add(ErrorDTO(message))
    }

    fun cleanErrors() {
        logger.info("Cleaning all errors")
        errors.clear()
    }

    fun getErrors(): List<ErrorDTO> {
        logger.debug("Retrieving ${errors.size} errors")
        return errors
    }
}
