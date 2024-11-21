package edu.ingsis.printscriptService.external.asset

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.util.logging.Level
import java.util.logging.Logger

@Component
class AssetService(
    @Value("\${services.asset.url}") val baseUrl: String,
) : AssetAPI {

    private val logger: Logger = Logger.getLogger(AssetService::class.java.name)
    lateinit var webClient: WebClient

    @PostConstruct
    fun init() {
        webClient = WebClient.create(baseUrl)
    }

    override fun getAsset(
        container: String,
        key: String,
    ): Mono<String> {
        logger.log(Level.INFO, "Fetching asset from container: $container with key: $key")
        return webClient.get()
            .uri("/v1/asset/$container/$key")
            .retrieve()
            .bodyToMono(String::class.java)
            .doOnSuccess { response ->
                logger.log(Level.INFO, "Successfully fetched asset: $key from container: $container")
            }
            .doOnError { error ->
                handleError("Error fetching asset $key from container $container", error)
            }
    }

    override fun createAsset(
        container: String,
        key: String,
        content: String,
    ): Mono<Void> {
        logger.log(Level.INFO, "Creating asset in container: $container with key: $key")
        return webClient.put()
            .uri("/v1/asset/$container/$key")
            .bodyValue(content)
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess {
                logger.log(Level.INFO, "Successfully created asset: $key in container: $container")
            }
            .doOnError { error ->
                handleError("Error creating asset $key in container $container", error)
            }
            .then()
    }

    override fun deleteAsset(
        container: String,
        key: String,
    ): Mono<Void> {
        logger.log(Level.INFO, "Deleting asset from container: $container with key: $key")
        return webClient.delete()
            .uri("/v1/asset/$container/$key")
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess {
                logger.log(Level.INFO, "Successfully deleted asset: $key from container: $container")
            }
            .doOnError { error ->
                handleError("Error deleting asset $key from container $container", error)
            }
            .then()
    }

    private fun handleError(message: String, error: Throwable) {
        if (error is WebClientResponseException) {
            logger.log(
                Level.SEVERE,
                "$message. Status: ${error.statusCode}, Body: ${error.responseBodyAsString}",
                error
            )
        } else {
            logger.log(Level.SEVERE, message, error)
        }
    }
}
