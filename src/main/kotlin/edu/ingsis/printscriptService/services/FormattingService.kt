package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.FormatResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream
import java.io.StringWriter

@Component
class FormattingService @Autowired constructor(
    private val assetService: AssetService
) {

    private companion object {
        const val SNIPPETS_CONTAINER = "snippets"
    }

    fun format(snippetId: String, configId: String): FormatResultDTO {
        val snippet = assetService.getAsset(SNIPPETS_CONTAINER, snippetId).block()
            ?: throw RuntimeException("Snippet with ID $snippetId not found")
        val config = assetService.getAsset(SNIPPETS_CONTAINER, configId).block()
            ?: throw RuntimeException("Config with ID $configId not found")

        val runner = Runner()
        val errorHandler = ValidateErrorHandler()
        val writer = StringWriter()

        runner.runFormat(
            ByteArrayInputStream(snippet.toByteArray()),
            "1.1",
            writer,
            ByteArrayInputStream(config.toByteArray()),
            errorHandler
        )

        val formattedContent = if (errorHandler.getErrors().isEmpty()) writer.toString() else null
        return FormatResultDTO(snippetId = snippetId.toLong(), formattedContent = formattedContent)
    }
}
