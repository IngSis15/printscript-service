package edu.ingsis.printscriptService.controllers

import edu.ingsis.printscriptService.dto.ExecuteRequestDTO
import edu.ingsis.printscriptService.dto.ExecuteResultDTO
import edu.ingsis.printscriptService.services.ExecutionService
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

    @PostMapping
    fun execute(@RequestBody dto: ExecuteRequestDTO): ResponseEntity<ExecuteResultDTO> {
        return ResponseEntity.ok(service.execute(dto.container, dto.key, dto.input))
    }
}
