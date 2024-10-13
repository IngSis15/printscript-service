package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.DTO.ExecuteRequestDTO
import edu.ingsis.printscriptService.DTO.ExecuteResultDTO
import edu.ingsis.printscriptService.DTO.RequestDTO
import edu.ingsis.printscriptService.DTO.ResultDTO
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
    fun analyze(@RequestBody dto: RequestDTO): ResponseEntity<ResultDTO> {
        return ResponseEntity.ok(service.analyze(dto.snippet, dto.version))
    }

    @PostMapping("/execute")
    fun execute(@RequestBody dto: ExecuteRequestDTO): ResponseEntity<ExecuteResultDTO> {
        return ResponseEntity.ok(service.execute(dto.snippet, dto.version, dto.input))
    }

//    @PostMapping("/lint")
//    fun lint(@RequestBody dto: RequestDTO): ResponseEntity<LintResultDTO> {
//        return ResponseEntity.ok(service.lint(dto.snippet, dto.version))
//    }
}
