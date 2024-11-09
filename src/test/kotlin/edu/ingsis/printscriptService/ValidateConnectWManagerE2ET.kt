package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.dto.ErrorDTO
import edu.ingsis.printscriptService.dto.ResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
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

    @MockBean
    private lateinit var assetService: AssetService

    @Test
    fun `test validate by id invalid`() {
        val snippetId = 1L
        val resultDTO = ResultDTO(false, listOf(ErrorDTO("Snippet is not valid")))

        Mockito.`when`(service.validateById(snippetId)).thenReturn(resultDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/validate/$snippetId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("Snippet is not valid"))
    }

    @Test
    fun `test validate by id valid`() {
        val snippetId = 3L
        val resultDTO = ResultDTO(true, emptyList())

        Mockito.`when`(service.validateById(snippetId)).thenReturn(resultDTO)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/validate/$snippetId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty)
    }
}
