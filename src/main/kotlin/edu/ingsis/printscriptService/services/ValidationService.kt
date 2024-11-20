package edu.ingsis.printscriptService.services

import edu.ingsis.printscriptService.dto.ResultDTO
import edu.ingsis.printscriptService.utils.ValidateErrorHandler
import org.springframework.stereotype.Component
import runner.Runner
import java.io.ByteArrayInputStream

@Component
class ValidationService {
    fun validate(snippet: String): ResultDTO {
        val runner = Runner()
        val errorHandler = ValidateErrorHandler()

        runner.runValidate(
            ByteArrayInputStream(snippet.toByteArray()),
            "1.1",
            errorHandler
        )

        return ResultDTO(errorHandler.getErrors().isEmpty(), errorHandler.getErrors())
    }
}
