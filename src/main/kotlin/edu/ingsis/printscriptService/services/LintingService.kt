package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.LintResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream

@Component
class LintingService @Autowired constructor(
    private val assetService: AssetService
) {
    private val runner = Runner()
    private val errorHandler = ValidateErrorHandler()

    private companion object {
        const val SNIPPETS_CONTAINER = "snippets"
    }

    fun lint(snippetId: String, configId: String): LintResultDTO {
        val snippet = assetService.getAsset(SNIPPETS_CONTAINER, snippetId).block()
            ?: throw RuntimeException("Snippet with ID $snippetId not found")
        val config = assetService.getAsset(SNIPPETS_CONTAINER, configId).block()
            ?: throw RuntimeException("Config with ID $configId not found")

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
