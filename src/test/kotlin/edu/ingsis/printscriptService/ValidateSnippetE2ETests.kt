package edu.ingsis.printscriptService

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.DTO.RequestDTO
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
class ValidateSnippetE2ETests {

    @Autowired
    lateinit var mockMvc: MockMvc

    companion object {
        @JvmStatic
        fun data(): Stream<Arguments> {
            return Stream.of(
                // Version 1.0
                Arguments.of("test-valid-assignation", "1.0", true),
                Arguments.of("test-invalid-assignation", "1.0", false),
                Arguments.of("test-valid-operation", "1.0", true),
                Arguments.of("test-invalid-operation", "1.0", false),
                Arguments.of("test-valid-reassignation", "1.0", true),
                Arguments.of("test-invalid-reassignation", "1.0", false),

                // Version 1.1
                Arguments.of("test-valid-condition", "1.1", true),
                Arguments.of("test-invalid-condition", "1.1", false),
                Arguments.of("test-invalid-const-reassignation", "1.1", false),

            )
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `test validate snippets`(directory: String, version: String, expectedOk: Boolean) {
        val snippet = readLines("src/test/resources/validate/$version/$directory/snippet.ps").joinToString("\n")

        val body = RequestDTO(snippet, version)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(body))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.ok").value(expectedOk))
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
