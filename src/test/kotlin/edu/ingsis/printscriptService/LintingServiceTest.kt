package edu.ingsis.printscriptService

import edu.ingsis.printscriptService.dto.LintResultDTO
import edu.ingsis.printscriptService.external.asset.AssetService
import edu.ingsis.printscriptService.services.LintingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
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
class LintingServiceTest {

    private val assetService: AssetService = mock(AssetService::class.java)
    private val lintingService = LintingService(assetService)

    @Test
    fun `should return ok false when config has errors`() {
        `when`(assetService.getAsset(anyOrNull(), anyOrNull())).thenReturn(Mono.just("valid snippet content"))
        `when`(assetService.getAsset(anyOrNull(), anyOrNull())).thenReturn(Mono.just("invalid config content"))

        val result: LintResultDTO = lintingService.lint("1", "2")

        assertFalse(result.ok, "Expected OK to be false for invalid config")
        assertEquals(1, result.snippetId)
    }
}