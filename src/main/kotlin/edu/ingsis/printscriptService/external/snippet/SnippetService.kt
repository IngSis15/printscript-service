package edu.ingsis.printscriptService.external.snippet

import edu.ingsis.printscriptService.external.snippet.dto.StatusDto
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class SnippetService(
    @Value("\${services.snippet.url}") val baseUrl: String,
) : SnippetApi {
    private lateinit var webClient: WebClient

    @PostConstruct
    fun init() {
        webClient = WebClient.create(baseUrl)
    }

    override fun updateLintStatus(statusDto: StatusDto): Mono<Void> {
        return webClient.post()
            .uri("/v1/snippet/status")
            .bodyValue(statusDto)
            .retrieve()
            .toBodilessEntity()
            .then()
    }
}
