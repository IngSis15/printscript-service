package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.LintResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import runner.Runner
import java.io.ByteArrayInputStream

@Component
class LintingService @Autowired constructor(
    private val assetService: AssetService,
) {
    private val logger = LoggerFactory.getLogger(LintingService::class.java)
    private val runner = Runner()
    private val errorHandler = ValidateErrorHandler()

    fun lint(snippetId: String, configId: String): LintResultDTO {
        logger.info("Request received to lint snippet with ID: $snippetId using config ID: $configId")

        val snippet = assetService.getAsset("snippets", snippetId)
        if (snippet == null) {
            logger.error("Snippet with ID $snippetId not found")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet with ID $snippetId not found")
        }
        logger.info("Snippet with ID $snippetId found successfully")

        val config = assetService.getAsset("linting", configId)
        if (config == null) {
            logger.error("Config with ID $configId not found")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Config with ID $configId not found")
        }
        logger.info("Config with ID $configId found successfully")

        errorHandler.cleanErrors()

        logger.info("Starting linting process for snippet ID: $snippetId using config ID: $configId")
        try {
            runner.runAnalyze(
                ByteArrayInputStream(snippet.toByteArray()),
                "1.1",
                ByteArrayInputStream(config.toByteArray()),
                errorHandler
            )
        } catch (e: Exception) {
            logger.error("Error during linting process for snippet ID: $snippetId", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during linting process")
        }

        val isOk = errorHandler.getErrors().isEmpty()
        logger.info("Linting result for snippet ID: $snippetId - Passed: $isOk")

        return LintResultDTO(snippetId = snippetId.toLong(), ok = isOk)
    }
}
