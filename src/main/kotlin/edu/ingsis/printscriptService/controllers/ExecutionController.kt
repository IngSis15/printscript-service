package edu.ingsis.printscriptService.controllers

import edu.ingsis.printscriptService.DTO.ExecuteRequestDTO
import edu.ingsis.printscriptService.DTO.ExecuteResultDTO
import edu.ingsis.printscriptService.services.ExecutionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
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
        return ResponseEntity.ok(service.execute(dto.snippet, dto.version, dto.input))
    }

    @PostMapping("/{id}")
    fun execute(@PathVariable id: Long, @RequestBody input: List<String>): ResponseEntity<ExecuteResultDTO> {
        return ResponseEntity.ok(service.executeById(id, input))
    }
}