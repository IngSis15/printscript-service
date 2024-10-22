package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.DTO.ResultDTO
import edu.ingsis.printscriptService.errorHandler.ValidateErrorHandler
import edu.ingsis.printscriptService.external.manager.ManagerAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream

@Component
class ValidationService @Autowired constructor(private val snippetManager: ManagerAPI) {

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
}
