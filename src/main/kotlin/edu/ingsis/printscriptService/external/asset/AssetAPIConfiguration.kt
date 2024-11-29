package edu.ingsis.printscriptService.external.asset

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AssetAPIConfiguration {
    @Bean
    @Profile("!test")
    fun assetApi(
        @Value("\${services.asset.url}") baseUrl: String,
        restTemplate: RestTemplate
    ): AssetAPI {
        return AssetService(baseUrl, restTemplate)
    }
}
