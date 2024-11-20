package edu.ingsis.printscriptService.dto

data class ExecuteRequestDTO(
    val container: String,
    val key: String,
    val input: List<String>
)
