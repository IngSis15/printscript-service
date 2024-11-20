package edu.ingsis.printscriptService.redis.consumer.format

import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.redis.consumer.format.dto.FormatSnippetDto
import edu.ingsis.printscriptService.services.FormattingService
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

@Component
@Profile("!test")
class FormatSnippetConsumer @Autowired constructor(
    redisTemplate: RedisTemplate<String, String>,
    @Value("\${stream.format.key}") streamKey: String,
    @Value("\${groups.format}") groupId: String,
    private val formattingService: FormattingService,
    private val assetService: AssetService
) : RedisStreamConsumer<String>(streamKey, groupId, redisTemplate) {

    private val logger = getLogger(FormatSnippetConsumer::class.simpleName)

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(java.time.Duration.ofMillis(10000)) // Set poll rate
            .targetType(String::class.java) // Set type to de-serialize record
            .build()
    }

    override fun onMessage(record: ObjectRecord<String, String>) {
        val formatSnippetDto = Json.decodeFromString<FormatSnippetDto>(record.value)
        logger.log(System.Logger.Level.INFO, "Formatting snippet: ${formatSnippetDto.snippetId}")

        val result = formattingService.format(formatSnippetDto.snippetId.toString(), formatSnippetDto.configId)
        assetService.createAsset("formatted", result.snippetId.toString(), result.formattedContent).block()
    }
}
