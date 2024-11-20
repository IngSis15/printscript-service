package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.LintResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
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
    private val runner = Runner()
    private val errorHandler = ValidateErrorHandler()

    fun lint(snippetId: String, configId: String): LintResultDTO {
        val snippet = assetService.getAsset("snippets", snippetId).block()
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Snippet with ID $snippetId not found"
            )
        val config = assetService.getAsset("linting", configId).block()
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Config with ID $configId not found"
            )

        errorHandler.cleanErrors()

        runner.runAnalyze(
            ByteArrayInputStream(snippet.toByteArray()),
            "1.1",
            ByteArrayInputStream(config.toByteArray()),
            errorHandler
        )

        val isOk = errorHandler.getErrors().isEmpty()
        return LintResultDTO(snippetId = snippetId.toLong(), ok = isOk)
    }
}
