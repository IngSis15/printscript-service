package edu.ingsis.printscriptService.redis.consumer.lint

import edu.ingsis.printscriptService.redis.consumer.config.RedisStreamConsumer
import edu.ingsis.printscriptService.redis.consumer.lint.dto.LintSnippetDto
import edu.ingsis.printscriptService.redis.producer.status.StatusService
import edu.ingsis.printscriptService.services.LintingService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.lang.System.getLogger

@Component
@Profile("!test")
class LintSnippetConsumer(
    @Value("\${stream.lint.key}") streamKey: String,
    private val redisTemplate: RedisTemplate<String, String>,
    private val lintingService: LintingService,
    private val statusService: StatusService
) : RedisStreamConsumer<String>(streamKey, "lint-group", redisTemplate) {

    private val logger = getLogger(LintSnippetConsumer::class.simpleName)

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(java.time.Duration.ofMillis(30000)) // Set poll rate
            .targetType(String::class.java) // Set type to de-serialize record
            .build()
    }

    override fun onMessage(record: ObjectRecord<String, String>) {
        val lintingSnippetDto = Json.decodeFromString<LintSnippetDto>(record.value)
        logger.log(System.Logger.Level.INFO, "Linting snippet: ${lintingSnippetDto.snippetId}")

        Mono.fromCallable {
            lintingService.lint(lintingSnippetDto.snippetId.toString(), lintingSnippetDto.configId)
        }
            .flatMap { lintResultDTO ->
                Mono.fromCallable {
                    statusService.sendStatus(lintResultDTO)
                }.publishOn(Schedulers.boundedElastic())
            }
            .doOnError { e ->
                logger.log(System.Logger.Level.ERROR, "Error processing message: ${e.message}", e)
            }
            .subscribe()
    }
}
