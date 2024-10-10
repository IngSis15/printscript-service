package edu.ingsis.printscriptService.errorHandler

import edu.ingsis.printscriptService.DTO.ErrorDTO
import runner.ErrorHandler

class AnalyzeErrorHandler : ErrorHandler {
    private val errors = mutableListOf<ErrorDTO>()

    override fun handleError(message: String) {
        errors.add(ErrorDTO(message))
    }

    fun getErrors(): List<ErrorDTO> {
        return errors
    }
}
