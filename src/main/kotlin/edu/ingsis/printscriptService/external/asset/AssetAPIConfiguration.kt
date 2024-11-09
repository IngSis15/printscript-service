package edu.ingsis.printscriptService.external.asset

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

class AssetAPIConfiguration {
    @Bean
    @Profile("!test")
    fun assetApi(
        @Value("\${services.asset.url}") baseUrl: String,
    ): AssetAPI {
        return AssetService(baseUrl)
    }
}
