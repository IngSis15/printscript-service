package edu.ingsis.printscriptService.analyzeSnippet

import edu.ingsis.printscriptService.utils.StringToStream
import org.springframework.stereotype.Service
import runner.Runner

@Service
class AnalyzeService {

    fun analyze(snippet: String, version: String): ErrorDTO? {
        val runner = Runner()
        val errorHandler = AnalyzeErrorHandler()
        val stringToStream = StringToStream()
        val stream = stringToStream.convertStringToStream(snippet)

        runner.runValidate(stream, version, errorHandler)

        return errorHandler.getError()
    }
}
