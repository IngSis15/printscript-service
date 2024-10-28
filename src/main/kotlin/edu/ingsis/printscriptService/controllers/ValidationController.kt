package edu.ingsis.printscriptService.controllers

import edu.ingsis.printscriptService.DTO.RequestDTO
import edu.ingsis.printscriptService.DTO.ResultDTO
import edu.ingsis.printscriptService.services.ValidationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/validate")
class ValidationController {

    @Autowired
    lateinit var service: ValidationService

    @PostMapping
    fun validate(@RequestBody dto: RequestDTO): ResponseEntity<ResultDTO> {
        return ResponseEntity.ok(service.validate(dto.snippet, dto.version))
    }

    @PostMapping("/{id}")
    fun validate(@PathVariable id: Long): ResponseEntity<ResultDTO> {
        return ResponseEntity.ok(service.validateById(id))
    }

//    @PostMapping("/lint")
//    fun lint(@RequestBody dto: RequestDTO): ResponseEntity<LintResultDTO> {
//        return ResponseEntity.ok(service.lint(dto.snippet, dto.version))
//    }
}
