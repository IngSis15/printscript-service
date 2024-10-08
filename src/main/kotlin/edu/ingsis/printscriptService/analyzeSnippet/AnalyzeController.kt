package edu.ingsis.printscriptService.analyzeSnippet

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/analyze")
class AnalyzeController {

    @Autowired
    lateinit var analyzeService:AnalyzeService

    @PostMapping("")
    fun analyze(@RequestBody dto : AnalyzeDTO ): ResponseEntity<ErrorDTO> {
        val errorDTO = analyzeService.analyze(dto.snippet, dto.version)
        return if (errorDTO != null) {
            ResponseEntity.ok(errorDTO)
        } else {
            ResponseEntity.noContent().build()
        }
    }
}