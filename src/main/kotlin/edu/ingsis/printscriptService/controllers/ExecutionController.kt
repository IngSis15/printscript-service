package edu.ingsis.printscriptService.controllers

import edu.ingsis.printscriptService.dto.ExecuteRequestDTO
import edu.ingsis.printscriptService.dto.ExecuteResultDTO
import edu.ingsis.printscriptService.services.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/execute")
class ExecutionController {

    @Autowired
    lateinit var service: ExecutionService

    private val logger = LoggerFactory.getLogger(ExecutionController::class.java)

    @PostMapping
    fun execute(@RequestBody dto: ExecuteRequestDTO): ResponseEntity<ExecuteResultDTO> {
        logger.info("Received execute request: container='{}', key='{}', inputSize={}", dto.container, dto.key, dto.input.size)

        return try {
            val response = service.execute(dto.container, dto.key, dto.input)
            logger.info("Execution succeeded for container='{}', key='{}'", dto.container, dto.key)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            logger.error("Execution failed for container='{}', key='{}'. Error: {}", dto.container, dto.key, e.message, e)
            ResponseEntity.internalServerError().build()
        }
    }
}
