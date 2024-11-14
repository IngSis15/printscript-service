package edu.ingsis.printscriptService.redis.producer.status

import edu.ingsis.printscriptService.redis.producer.config.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.lang.System.getLogger

interface LintStatusProducer {
    fun publishEvent(event: String)
}

@Component
class RedisLintStatusProducer
@Autowired
constructor(
    @Value("\${stream.status.key}") streamKey: String,
    redis: RedisTemplate<String, String>,
) : LintStatusProducer, RedisStreamProducer(streamKey, redis) {
    val logger: System.Logger = getLogger(LintStatusProducer::class.simpleName)

    override fun publishEvent(event: String) {
        logger.log(System.Logger.Level.INFO, "Sending status: $event")
        emit(event)
    }
}
