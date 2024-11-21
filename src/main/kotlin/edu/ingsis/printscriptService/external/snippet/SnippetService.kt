package edu.ingsis.printscriptService.external.snippet

import edu.ingsis.printscriptService.external.snippet.dto.StatusDto
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.util.logging.Level
import java.util.logging.Logger

class SnippetService(
    @Value("\${services.snippet.url}") val baseUrl: String,
) : SnippetApi {

    private lateinit var webClient: WebClient
    private val logger: Logger = Logger.getLogger(SnippetService::class.java.name)

    @PostConstruct
    fun init() {
        logger.log(Level.INFO, "Initializing WebClient for SnippetService with base URL: $baseUrl")
        webClient = WebClient.create(baseUrl)
    }

    override fun updateLintStatus(statusDto: StatusDto): Mono<Void> {
        logger.log(Level.INFO, "Updating lint status with payload: $statusDto")

        return webClient.post()
            .uri("/v1/snippet/status")
            .bodyValue(statusDto)
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess {
                logger.log(Level.INFO, "Successfully updated lint status for snippet: ${statusDto.snippetId}")
            }
            .doOnError { error ->
                handleError("Error updating lint status for snippet: ${statusDto.snippetId}", error)
            }
            .then()
    }

    private fun handleError(message: String, error: Throwable) {
        if (error is WebClientResponseException) {
            logger.log(
                Level.SEVERE,
                "$message. HTTP Status: ${error.statusCode}, Response Body: ${error.responseBodyAsString}",
                error
            )
        } else {
            logger.log(Level.SEVERE, message, error)
        }
    }
}
