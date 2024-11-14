package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.dto.FormatResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.services.FormattingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Mono

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class FormattingServiceTest {

    private val assetService: AssetService = mock(AssetService::class.java)
    private val formattingService = FormattingService(assetService)

    @Test
    fun `should return formatted content when snippet and config are valid`() {
        `when`(assetService.createAsset(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(Mono.empty())
        `when`(assetService.getAsset("snippets", "1")).thenReturn(Mono.just("valid snippet"))
        `when`(assetService.getAsset("formatting", "2")).thenReturn(Mono.just("valid config"))

        val result: FormatResultDTO = formattingService.format("1", "2").block()!!

        assertEquals(1, result.snippetId, "Snippet ID should match.")
    }

    @Test
    fun `should return null formatted content when config has errors`() {
        `when`(assetService.createAsset(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(Mono.empty())
        `when`(assetService.getAsset("snippets", "1")).thenReturn(Mono.just("valid snippet"))
        `when`(assetService.getAsset("formatting", "1")).thenReturn(Mono.just("invalid config"))

        val result: FormatResultDTO = formattingService.format("1", "1").block()!!

        assertEquals("", result.formattedContent, "Formatted content should be null if config is invalid.")
        assertEquals(1, result.snippetId, "Snippet ID should match.")
    }

    @Test
    fun `should throw exception when snippet not found`() {
        `when`(assetService.getAsset("snippets", "snippetKey")).thenReturn(Mono.empty())
        `when`(assetService.getAsset("snippets", "configKey")).thenReturn(Mono.just("valid config"))

        assertThrows(RuntimeException::class.java) {
            formattingService.format("1", "1")
        }
    }

    @Test
    fun `should throw exception when config not found`() {
        // Mocking config not found (Mono.empty() for config)
        `when`(assetService.getAsset("snippets", "snippetKey")).thenReturn(Mono.just("valid snippet"))
        `when`(assetService.getAsset("snippets", "configKey")).thenReturn(Mono.empty())

        assertThrows(RuntimeException::class.java) {
            formattingService.format("1", "1")
        }
    }
}
