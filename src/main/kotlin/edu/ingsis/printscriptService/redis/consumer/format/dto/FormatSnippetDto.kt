package edu.ingsis.printscriptService.redis.consumer.format.dto

import kotlinx.serialization.Serializable

@Serializable
class FormatSnippetDto(
    val snippetId: Long,
    val configId: String,
    val correlationId: String,
)
