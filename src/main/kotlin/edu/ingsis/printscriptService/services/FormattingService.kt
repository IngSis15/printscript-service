package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.FormatResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
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

    fun format(snippetId: String, configId: String): FormatResultDTO {
        val snippet = assetService.getAsset("snippets", snippetId).block()
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Snippet with ID $snippetId not found"
            )
        val config = assetService.getAsset("formatting", configId).block()
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Config with ID $configId not found"
            )

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

        val formattedContent = writer.toString()
        return FormatResultDTO(snippetId = snippetId.toLong(), formattedContent = formattedContent)
    }
}
