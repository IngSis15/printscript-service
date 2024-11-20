package edu.ingsis.printscriptService.utils

import edu.ingsis.printscriptService.dto.ErrorDTO
import runner.ErrorHandler

class ValidateErrorHandler : ErrorHandler {
    private val errors = mutableListOf<ErrorDTO>()

    override fun handleError(message: String) {
        errors.add(ErrorDTO(message))
    }

    fun cleanErrors() {
        errors.clear()
    }

    fun getErrors(): List<ErrorDTO> {
        return errors
    }
}
