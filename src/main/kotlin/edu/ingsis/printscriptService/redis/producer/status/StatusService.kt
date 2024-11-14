package edu.ingsis.printscriptService.redis.producer.status

import edu.ingsis.printscriptService.dto.LintResultDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StatusService
@Autowired
constructor(
    private val lintStatusProducer: LintStatusProducer,
) {
    fun sendStatus(
        lintResultDto: LintResultDTO
    ) {
        lintStatusProducer.publishEvent(Json.encodeToString(lintResultDto))
    }
}
