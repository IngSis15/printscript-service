package edu.ingsis.printscriptService.service

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.DTO.RequestDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class AnalyzeSnippetE2ETests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `test valid snippet passes validation`() {
        val body = RequestDTO("let x: number = 5;\nprintln(x);", "1.0")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(body))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(true))
    }

    @Test
    fun `test invalid snippet fails validation`() {
        val snippet = "let x number = 5;\nx = \"hello\";"
        val body = RequestDTO(snippet, "1.0")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(body))

        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(false))
    }
}
