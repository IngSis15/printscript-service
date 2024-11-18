package edu.ingsis.printscriptService.redis.consumer.lint

import edu.ingsis.printscriptService.external.snippet.SnippetApi
import edu.ingsis.printscriptService.external.snippet.dto.Compliance
import edu.ingsis.printscriptService.external.snippet.dto.StatusDto
import edu.ingsis.printscriptService.redis.consumer.lint.dto.LintSnippetDto
import edu.ingsis.printscriptService.services.LintingService
import kotlinx.serialization.json.Json
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.lang.System.getLogger
import java.time.Duration

@Component
@Profile("!test")
class LintSnippetConsumer @Autowired constructor(
    redisTemplate: RedisTemplate<String, String>,
    @Value("\${stream.lint.key}") streamKey: String,
    @Value("\${groups.lint}") groupId: String,
    private val lintingService: LintingService,
    private val snippetApi: SnippetApi
) : RedisStreamConsumer<String>(streamKey, groupId, redisTemplate) {

    private val logger = getLogger(LintSnippetConsumer::class.simpleName)

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(String::class.java) // Set type to de-serialize record
            .build()
    }

    override fun onMessage(record: ObjectRecord<String, String>) {
        val lintingSnippetDto = Json.decodeFromString<LintSnippetDto>(record.value)
        logger.log(System.Logger.Level.INFO, "Linting snippet: ${lintingSnippetDto.snippetId}")
        val result = lintingService.lint(lintingSnippetDto.snippetId.toString(), lintingSnippetDto.configId)

        if (result.ok) {
            snippetApi.updateLintStatus(StatusDto(lintingSnippetDto.snippetId, Compliance.COMPLIANT)).subscribe()
        } else {
            snippetApi.updateLintStatus(StatusDto(lintingSnippetDto.snippetId, Compliance.NON_COMPLIANT)).subscribe()
        }
    }
}
