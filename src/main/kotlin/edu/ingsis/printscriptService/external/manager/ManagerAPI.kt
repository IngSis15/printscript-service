package edu.ingsis.printscriptService.external.manager

import edu.ingsis.printscriptService.external.manager.DTO.GetSnippetDTO
import reactor.core.publisher.Mono

interface ManagerAPI {
    fun get(id: Long): Mono<GetSnippetDTO>
}
