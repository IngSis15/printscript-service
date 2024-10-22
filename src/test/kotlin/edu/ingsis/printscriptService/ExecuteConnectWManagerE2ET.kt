package edu.ingsis.printscriptService

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.DTO.ExecuteResultDTO
import edu.ingsis.printscriptService.services.ExecutionService
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ExecuteConnectWManagerE2ET {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: ExecutionService

    @Test
    fun `test execute by id`() {
        val snippetId = 2L
        val input = listOf("1", "2", "3")
        val resultDTO = ExecuteResultDTO(listOf("bye"))

        Mockito.`when`(service.executeById(snippetId, input)).thenReturn(resultDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/execute/$snippetId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(input))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.result[0]").value("bye"))
    }
}