package edu.ingsis.printscriptService.service

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ingsis.printscriptService.DTO.ExecuteRequestDTO
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
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
class ExecuteSnippetE2ETests {

    @Autowired
    lateinit var mockMvc: MockMvc

    companion object {
        @JvmStatic
        fun data(): Stream<Arguments> {
            return Stream.of(
                // Version 1.0
                Arguments.of("test-hello", "1.0"),
                Arguments.of("test-assignment", "1.0"),
                Arguments.of("test-complex-operation", "1.0"),
                Arguments.of("test-operation", "1.0"),
                Arguments.of("test-concat-string-number", "1.0"),
                Arguments.of("test-decimal", "1.0"),
                Arguments.of("test-declaration", "1.0"),

                // Version 1.1
                Arguments.of("test-conditional", "1.1"),
                Arguments.of("test-conditional-variable", "1.1"),
                Arguments.of("test-const", "1.1"),
                Arguments.of("test-many-inputs", "1.1"),
                Arguments.of("test-readinput", "1.1"),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `test execute snippets`(directory: String, version: String) {
        val snippet = readLines("src/test/resources/execute/$version/$directory/main.ps").joinToString("\n")
        val expected = readLines("src/test/resources/execute/$version/$directory/expected.txt").joinToString("\n")
        val input = readLinesIfExists("src/test/resources/execute/$version/$directory/input.txt").orElse(emptyList())

        val body = ExecuteRequestDTO(snippet, version, input)

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
