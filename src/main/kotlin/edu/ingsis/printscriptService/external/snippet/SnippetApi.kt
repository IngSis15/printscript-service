package edu.ingsis.printscriptService.external.snippet

import edu.ingsis.printscriptService.external.snippet.dto.StatusDto
import reactor.core.publisher.Mono

interface SnippetApi {
    fun updateLintStatus(statusDto: StatusDto): Mono<Void>
}
