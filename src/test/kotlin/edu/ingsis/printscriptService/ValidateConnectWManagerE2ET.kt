package edu.ingsis.printscriptService

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.DTO.ErrorDTO
import edu.ingsis.printscriptService.DTO.ResultDTO
import edu.ingsis.printscriptService.services.ValidationService
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
class ValidateConnectWManagerE2ET {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: ValidationService

    @Test
    fun `test validate by id invalid`() {
        val snippetId = 1L
        val resultDTO = ResultDTO(false, listOf(ErrorDTO("Snippet is not valid")))

        Mockito.`when`(service.validateById(snippetId)).thenReturn(resultDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/validate/$snippetId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(snippetId))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(false))
    }

    @Test
    fun `test validate by id valid`() {
        val snippetId = 3L
        val resultDTO = ResultDTO(true, emptyList())

        Mockito.`when`(service.validateById(snippetId)).thenReturn(resultDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/validate/$snippetId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(snippetId))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(true))
    }
}
