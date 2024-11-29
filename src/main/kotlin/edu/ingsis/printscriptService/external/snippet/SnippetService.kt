package edu.ingsis.printscriptService.external.snippet

import edu.ingsis.printscriptService.external.snippet.dto.StatusDto
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.util.logging.Level
import java.util.logging.Logger

@Service
@Profile("!test")
class SnippetService(
    @Value("\${services.snippet.url}") val baseUrl: String,
) : SnippetApi {

    private lateinit var restTemplate: RestTemplate
    private val logger: Logger = Logger.getLogger(SnippetService::class.java.name)

    @PostConstruct
    fun init() {
        restTemplate = RestTemplate()
    }

    override fun updateLintStatus(statusDto: StatusDto) {
        logger.log(Level.INFO, "Updating lint status with payload: $statusDto")
        val url = "$baseUrl/v1/snippet/status"
        try {
            val request = HttpEntity(statusDto)
            restTemplate.exchange(url, HttpMethod.POST, request, Void::class.java)
            logger.log(Level.INFO, "Successfully updated lint status for snippet: ${statusDto.snippetId}")
        } catch (ex: HttpClientErrorException) {
            handleError("Error updating lint status for snippet: ${statusDto.snippetId}", ex)
            throw ex
        } catch (ex: Exception) {
            handleError("Unexpected error updating lint status for snippet: ${statusDto.snippetId}", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
        }
    }

    private fun handleError(message: String, error: Throwable) {
        if (error is HttpClientErrorException) {
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
