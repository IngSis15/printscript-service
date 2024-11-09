package edu.ingsis.printscriptService.external.asset

import reactor.core.publisher.Mono

interface AssetAPI {
    fun getAsset(
        container: String,
        key: String,
    ): Mono<String>

    fun createAsset(
        container: String,
        key: String,
        content: String,
    ): Mono<Void>

    fun deleteAsset(
        container: String,
        key: String,
    ): Mono<Void>
}