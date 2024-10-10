package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.DTO.AnalyzeResultDTO
import edu.ingsis.printscriptService.DTO.ExecuteResultDTO
import edu.ingsis.printscriptService.errorHandler.AnalyzeErrorHandler
import org.springframework.stereotype.Service
import runner.Runner
import java.io.ByteArrayInputStream
import java.util.LinkedList

@Service
class Service {

    fun analyze(snippet: String, version: String): AnalyzeResultDTO {
        val runner = Runner()
        val errorHandler = AnalyzeErrorHandler()

        runner.runValidate(
            ByteArrayInputStream(snippet.toByteArray()),
            version,
            errorHandler
        )

        return AnalyzeResultDTO(errorHandler.getErrors().isEmpty(), errorHandler.getErrors())
    }

    fun execute(snippet: String, version: String, input: List<String>): ExecuteResultDTO {
        val runner = Runner()
        val errorHandler = AnalyzeErrorHandler()
        val queueInputProvider = QueueInputProvider(LinkedList(input))
        val printCollector = PrintCollector()

        runner.runExecute(
            ByteArrayInputStream(snippet.toByteArray()),
            version,
            errorHandler,
            printCollector,
            queueInputProvider
        )

        return ExecuteResultDTO(printCollector.getMessages())
    }
}
