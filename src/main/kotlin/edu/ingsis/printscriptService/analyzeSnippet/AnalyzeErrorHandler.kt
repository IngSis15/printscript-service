package edu.ingsis.printscriptService.analyzeSnippet

import runner.ErrorHandler

class AnalyzeErrorHandler: ErrorHandler {
    private  var errorDTO: ErrorDTO? = null
    override fun handleError(message: String) {
        errorDTO = ErrorDTO(message)
    }

    fun getError(): ErrorDTO? {
        return errorDTO
    }


}