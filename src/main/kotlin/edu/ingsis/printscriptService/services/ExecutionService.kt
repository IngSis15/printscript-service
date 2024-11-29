package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.ExecuteResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.utils.PrintCollector
import edu.ingsis.printscriptService.utils.QueueInputProvider
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream
import java.util.LinkedList

@Component
class ExecutionService @Autowired constructor(
    private val assetService: AssetService
) {

    private val logger = LoggerFactory.getLogger(ExecutionService::class.java)

    fun execute(container: String, key: String, input: List<String>): ExecuteResultDTO {
        logger.info("Starting execution for container='{}', key='{}'", container, key)

        val snippet: String = assetService.getAsset(container, key)
            ?: throw RuntimeException("Asset not found for container='$container', key='$key'")
        logger.info("Snippet retrieved successfully. Size: {} characters", snippet.length)

        val runner = Runner()
        val errorHandler = ValidateErrorHandler()
        val queueInputProvider = QueueInputProvider(LinkedList(input))
        val printCollector = PrintCollector()

        logger.info("Executing snippet for container='{}', key='{}'. Input size: {}", container, key, input.size)
        runner.runExecute(
            ByteArrayInputStream(snippet.toByteArray()),
            "1.1",
            errorHandler,
            printCollector,
            queueInputProvider
        )

        val result = ExecuteResultDTO(printCollector.getMessages())
        logger.info(
            "Execution completed successfully for container='{}', key='{}'. Result size: {}",
            container,
            key,
            result.result.size
        )

        return result
    }
}
