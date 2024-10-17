package edu.ingsis.printscriptService.external.manager

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class ManagerAPIConfiguration {
    @Bean
    @Profile("!test")
    fun managerAPI(
        @Value("\${services.snippetmanager.url}") baseUrl: String,
    ): ManagerAPI {
        return SnippetManager(baseUrl)
    }
}
