package edu.ingsis.printscriptService.external.snippet

import edu.ingsis.printscriptService.external.snippet.dto.StatusDto

interface SnippetApi {
    fun updateLintStatus(statusDto: StatusDto)
}
