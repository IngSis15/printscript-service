package edu.ingsis.printscriptService.external.asset

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.util.logging.Level
import java.util.logging.Logger

@Component
class AssetService(
    @Value("\${services.asset.url}") val baseUrl: String,
    val restTemplate: RestTemplate
) : AssetAPI {

    private val logger: Logger = Logger.getLogger(AssetService::class.java.name)

    override fun getAsset(container: String, key: String): String? {
        logger.log(Level.INFO, "Fetching asset from container: $container with key: $key")
        val url = "$baseUrl/v1/asset/$container/$key"
        return try {
            val response: ResponseEntity<String> = restTemplate.getForEntity(url, String::class.java)
            logger.log(Level.INFO, "Successfully fetched asset: $key from container: $container")
            response.body
        } catch (ex: HttpClientErrorException) {
            handleError("Error fetching asset $key from container $container", ex)
            throw ex
        } catch (ex: Exception) {
            handleError("Unexpected error fetching asset $key from container $container", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
        }
    }

    override fun createAsset(container: String, key: String, content: String) {
        logger.log(Level.INFO, "Creating asset in container: $container with key: $key")
        val url = "$baseUrl/v1/asset/$container/$key"
        try {
            val request = HttpEntity(content)
            restTemplate.exchange(url, HttpMethod.PUT, request, Void::class.java)
            logger.log(Level.INFO, "Successfully created asset: $key in container: $container")
        } catch (ex: HttpClientErrorException) {
            handleError("Error creating asset $key in container $container", ex)
            throw ex
        } catch (ex: Exception) {
            handleError("Unexpected error creating asset $key in container $container", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
        }
    }

    override fun deleteAsset(container: String, key: String) {
        logger.log(Level.INFO, "Deleting asset from container: $container with key: $key")
        val url = "$baseUrl/v1/asset/$container/$key"
        try {
            restTemplate.delete(url)
            logger.log(Level.INFO, "Successfully deleted asset: $key from container: $container")
        } catch (ex: HttpClientErrorException) {
            handleError("Error deleting asset $key from container $container", ex)
            throw ex
        } catch (ex: Exception) {
            handleError("Unexpected error deleting asset $key from container $container", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
        }
    }

    private fun handleError(message: String, error: Throwable) {
        if (error is HttpClientErrorException) {
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
