package edu.ingsis.printscriptService.external.manager

import edu.ingsis.printscriptService.external.manager.DTO.GetSnippetDTO
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class SnippetManager(
    @Value("\${services.manager.url") val baseUrl: String,

) : ManagerAPI {
    lateinit var webClient: WebClient

    @PostConstruct
    fun init() {
        webClient = WebClient.create(baseUrl)
    }

    override fun get(id: Long): Mono<GetSnippetDTO> {
        return webClient.get()
            .uri("/v1/snippet/$id")
            .retrieve()
            .bodyToMono(GetSnippetDTO::class.java)
    }
}
