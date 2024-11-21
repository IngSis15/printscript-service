package edu.ingsis.printscriptService.health

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Level
import java.util.logging.Logger

@RestController
@RequestMapping("/health")
class HealthController {

    private val logger: Logger = Logger.getLogger(HealthController::class.java.name)

    @GetMapping("")
    fun ping(): ResponseEntity<Unit> {
        logger.log(Level.FINE, "Health check received")
        return ResponseEntity.ok().build()
    }
}
