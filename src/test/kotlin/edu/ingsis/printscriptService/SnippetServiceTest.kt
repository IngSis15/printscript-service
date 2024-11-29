package edu.ingsis.printscriptService

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.external.snippet.SnippetService
import edu.ingsis.printscriptService.external.snippet.dto.Compliance
import edu.ingsis.printscriptService.external.snippet.dto.StatusDto
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import reactor.core.publisher.Mono

@WebMvcTest(SnippetService::class)
@AutoConfigureMockMvc
class SnippetServiceTest
@Autowired
constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var snippetService: SnippetService

    @Test
    fun `test updateLintStatus`() {
        val statusDto = StatusDto(12345L, Compliance.PENDING)
        val statusDtoJson = objectMapper.writeValueAsString(statusDto)

        Mockito.`when`(snippetService.updateLintStatus(statusDto)).thenAnswer {  }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/snippet/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusDtoJson)
        )
    }
}
