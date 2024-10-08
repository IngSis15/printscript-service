package edu.ingsis.printscriptService.analyzeSnippet

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ActiveProfiles(value = ["test"])
@AutoConfigureWebTestClient
class AnalyzeSnippetE2ETests {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `analyze snippet with error`() {
        val analyzeDTO = AnalyzeDTO("let x: number;\n" +
                "x = \"hello\";", "1.0")

        webTestClient.post()
            .uri("/v1/analyze")
            .bodyValue(analyzeDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.message").isEqualTo("An error occurred")
    }

    @Test
    fun `analyze snippet without error`() {
        val analyzeDTO = AnalyzeDTO("let x: number;\n" +
                "x = 42;", "1.0")

        webTestClient.post()
            .uri("/v1/analyze")
            .bodyValue(analyzeDTO)
            .exchange()
            .expectStatus().isNoContent
    }
}