package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.dto.LintResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.services.LintingService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Mono

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class LintingServiceTest {

    private val assetService: AssetService = mock(AssetService::class.java)
    private val lintingService = LintingService(assetService)

    @Test
    fun `should return ok true when snippet and config are valid`() {
        `when`(assetService.getAsset(anyOrNull(), anyOrNull())).thenReturn(Mono.just("valid snippet content"))
        `when`(assetService.getAsset(anyOrNull(), anyOrNull())).thenReturn(Mono.just("valid config"))

        val result: LintResultDTO = lintingService.lint("1", "2")

        assertTrue(result.ok, "Expected OK to be true for valid content")
        assertEquals(1, result.snippetId)
    }

    @Test
    fun `should return ok false when config has errors`() {
        `when`(assetService.getAsset(anyOrNull(), anyOrNull())).thenReturn(Mono.just("valid snippet content"))
        `when`(assetService.getAsset(anyOrNull(), anyOrNull())).thenReturn(Mono.just("invalid config content"))

        val result: LintResultDTO = lintingService.lint("1", "2")

        assertFalse(result.ok, "Expected OK to be false for invalid config")
        assertEquals(1, result.snippetId)
    }

    @Test
    fun `should throw exception when snippet not found`() {
        `when`(assetService.getAsset("container", "snippetKey")).thenReturn(Mono.empty())
        `when`(assetService.getAsset("container", "configKey")).thenReturn(Mono.just("valid config"))

        assertThrows(RuntimeException::class.java) {
            lintingService.lint("1", "2")
        }
    }

    @Test
    fun `should throw exception when config not found`() {
        `when`(assetService.getAsset("container", "snippetKey")).thenReturn(Mono.just("valid snippet"))
        `when`(assetService.getAsset("container", "configKey")).thenReturn(Mono.empty())

        assertThrows(RuntimeException::class.java) {
            lintingService.lint("1", "2")
        }
    }
}
