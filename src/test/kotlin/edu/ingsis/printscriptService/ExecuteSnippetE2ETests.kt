package edu.ingsis.printscriptService

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.dto.ExecuteRequestDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
import java.io.File
import java.io.FileNotFoundException
import java.util.Optional
import java.util.stream.Stream

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ExecuteSnippetE2ETests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var assetService: AssetService

    companion object {
        @JvmStatic
        fun data(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("test-hello", "1.0"),
                Arguments.of("test-assignment", "1.0"),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `test execute snippets`(directory: String, version: String) {
        val container = "snippets"
        val key = "$directory/main.ps"
        val snippetContent = readLines("src/test/resources/execute/$version/$directory/main.ps").joinToString("\n")
        val expected = readLines("src/test/resources/execute/$version/$directory/expected.txt").joinToString("\n")
        val input = readLinesIfExists("src/test/resources/execute/$version/$directory/input.txt").orElse(emptyList())

        Mockito.`when`(assetService.getAsset(container, key)).thenReturn(snippetContent)

        val body = ExecuteRequestDTO(container, key, input)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(body))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.result[0]").value(expected))
    }

    @Throws(FileNotFoundException::class)
    private fun readLines(filePath: String): List<String> {
        return readLinesIfExists(filePath).orElseThrow { FileNotFoundException(filePath) }
    }

    private fun readLinesIfExists(filePath: String): Optional<List<String>> {
        val file = File(filePath)
        if (file.exists()) {
            val list = file.readLines()
            return Optional.of(list)
        }
        return Optional.empty()
    }
}
