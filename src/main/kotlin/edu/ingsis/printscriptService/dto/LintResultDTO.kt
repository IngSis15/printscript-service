package edu.ingsis.printscriptService.dto

import kotlinx.serialization.Serializable

@Serializable
data class LintResultDTO(
    val snippetId: Long,
    val ok: Boolean,
)
