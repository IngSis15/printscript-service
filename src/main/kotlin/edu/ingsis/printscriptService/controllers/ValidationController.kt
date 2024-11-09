package edu.ingsis.printscriptService.controllers

import edu.ingsis.printscriptService.dto.ResultDTO
import edu.ingsis.printscriptService.services.ValidationService
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

    @PostMapping
    fun validate(@RequestBody snippet: String): ResponseEntity<ResultDTO> {
        return ResponseEntity.ok(service.validate(snippet))
    }
}
