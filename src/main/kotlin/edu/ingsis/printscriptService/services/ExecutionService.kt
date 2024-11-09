package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.ExecuteResultDTO
import edu.ingsis.printscriptService.PrintCollector
import edu.ingsis.printscriptService.QueueInputProvider
import edu.ingsis.printscriptService.errorHandler.ValidateErrorHandler
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.external.manager.ManagerAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream
import java.util.LinkedList

@Component
class ExecutionService @Autowired constructor(
    private val snippetManager: ManagerAPI,
    private val assetService: AssetService
) {

    fun execute(container: String, key: String, input: List<String>): ExecuteResultDTO {
        val snippet: String = assetService.getAsset(container, key).block() ?: throw RuntimeException("Asset not found")

        val runner = Runner()
        val errorHandler = ValidateErrorHandler()
        val queueInputProvider = QueueInputProvider(LinkedList(input))
        val printCollector = PrintCollector()

        runner.runExecute(
            ByteArrayInputStream(snippet.toByteArray()),
            "1.1",
            errorHandler,
            printCollector,
            queueInputProvider
        )

        return ExecuteResultDTO(printCollector.getMessages())
    }

    fun executeById(id: Long, input: List<String>): ExecuteResultDTO {
        val snippet = snippetManager.get(id).block()
        return execute(snippet!!.snippet, snippet.version, input)
    }
}
