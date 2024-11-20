package edu.ingsis.printscriptService.external.snippet

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
class SnippetApiConfiguration {
    @Bean
    @Profile("!test")
    fun snippetApi(
        @Value("\${services.snippet.url}") baseUrl: String,
    ): SnippetApi {
        return SnippetService(baseUrl)
    }
}
