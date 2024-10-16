package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.DTO.ExecuteResultDTO
import edu.ingsis.printscriptService.DTO.ResultDTO
import edu.ingsis.printscriptService.errorHandler.ValidateErrorHandler
import edu.ingsis.printscriptService.external.manager.ManagerAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream
import java.util.LinkedList

@Component
class Service @Autowired constructor(private val snippetManager: ManagerAPI) {

    fun validate(snippet: String, version: String): ResultDTO {
        val runner = Runner()
        val errorHandler = ValidateErrorHandler()

        runner.runValidate(
            ByteArrayInputStream(snippet.toByteArray()),
            version,
            errorHandler
        )

        return ResultDTO(errorHandler.getErrors().isEmpty(), errorHandler.getErrors())
    }

    fun validateById(id: Long): ResultDTO {
        val snippet = snippetManager.get(id).block()
        return validate(snippet!!.snippet, snippet.version)
    }

    fun execute(snippet: String, version: String, input: List<String>): ExecuteResultDTO {
        val runner = Runner()
        val errorHandler = ValidateErrorHandler()
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

    fun executeById(id: Long, input: List<String>): ExecuteResultDTO {
        val snippet = snippetManager.get(id).block()
        return execute(snippet!!.snippet, snippet.version, input)
    }
}
