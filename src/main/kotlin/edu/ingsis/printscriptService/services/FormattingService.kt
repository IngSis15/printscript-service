package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.FormatResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import runner.Runner
import java.io.ByteArrayInputStream
import java.io.StringWriter

@Component
class FormattingService @Autowired constructor(
    private val assetService: AssetService
) {

    private val logger = LoggerFactory.getLogger(FormattingService::class.java)

    fun format(snippetId: String, configId: String): FormatResultDTO {
        logger.info("Request received to format snippet with ID: $snippetId using config ID: $configId")

        val snippet = assetService.getAsset("snippets", snippetId)
        if (snippet == null) {
            logger.error("Snippet with ID $snippetId not found")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet with ID $snippetId not found")
        }
        logger.info("Snippet with ID $snippetId found successfully")

        val config = assetService.getAsset("formatting", configId)
        if (config == null) {
            logger.error("Config with ID $configId not found")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Config with ID $configId not found")
        }
        logger.info("Config with ID $configId found successfully")

        val runner = Runner()
        val errorHandler = ValidateErrorHandler()
        val writer = StringWriter()

        logger.info("Starting the format process for snippet ID: $snippetId using config ID: $configId")
        try {
            runner.runFormat(
                ByteArrayInputStream(snippet.toByteArray()),
                "1.1",
                writer,
                ByteArrayInputStream(config.toByteArray()),
                errorHandler
            )
        } catch (e: Exception) {
            logger.error("Error during format process for snippet ID: $snippetId", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during format process")
        }

        val formattedContent = writer.toString()

        logger.info("Successfully formatted snippet ID: $snippetId. Returning formatted content.")

        return FormatResultDTO(snippetId = snippetId.toLong(), formattedContent = formattedContent)
    }
}
