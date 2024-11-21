package edu.ingsis.printscriptService.controllers

import edu.ingsis.printscriptService.dto.ResultDTO
import edu.ingsis.printscriptService.services.ValidationService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/validate")
class ValidationController {

    @Autowired
    lateinit var service: ValidationService

    private val logger = LoggerFactory.getLogger(ValidationController::class.java)

    @PostMapping
    fun validate(@RequestBody snippet: String): ResponseEntity<ResultDTO> {
        logger.info("Received validation request. Snippet size: {} characters", snippet.length)

        val result = service.validate(snippet)
        logger.info(
            "Validation completed. Success: {}. Error count: {}",
            result.ok, result.errors.size
        )

        return ResponseEntity.ok(result)
    }
}
