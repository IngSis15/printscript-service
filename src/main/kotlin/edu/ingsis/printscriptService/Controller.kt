package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.DTO.AnalyzeResultDTO
import edu.ingsis.printscriptService.DTO.ExecuteRequestDTO
import edu.ingsis.printscriptService.DTO.ExecuteResultDTO
import edu.ingsis.printscriptService.DTO.RequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class Controller {

    @Autowired
    lateinit var service: Service

    @PostMapping("/analyze")
    fun analyze(@RequestBody dto: RequestDTO): ResponseEntity<AnalyzeResultDTO> {
        return ResponseEntity.ok(service.analyze(dto.snippet, dto.version))
    }

    @PostMapping("/execute")
    fun execute(@RequestBody dto: ExecuteRequestDTO): ResponseEntity<ExecuteResultDTO> {
        return ResponseEntity.ok(service.execute(dto.snippet, dto.version, dto.input))
    }
}
