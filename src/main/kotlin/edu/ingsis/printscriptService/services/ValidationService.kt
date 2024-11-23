package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.ResultDTO
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream

@Component
class ValidationService {

    private val logger = LoggerFactory.getLogger(ValidationService::class.java)

    fun validate(snippet: String): ResultDTO {
        logger.info("Starting validation for snippet. Size: {} characters", snippet.length)

        val runner = Runner()
        val errorHandler = ValidateErrorHandler()

        try {
            logger.debug("Initializing Runner for validation...")
            runner.runValidate(
                ByteArrayInputStream(snippet.toByteArray()),
                "1.1",
                errorHandler
            )
        } catch (e: Exception) {
            logger.error("Validation process failed due to an unexpected error: {}", e.message, e)
            throw RuntimeException("Validation failed", e)
        }

        val errors = errorHandler.getErrors()
        logger.info("Validation finished. Errors found: {}", errors.size)

        return ResultDTO(errors.isEmpty(), errors)
    }
}
