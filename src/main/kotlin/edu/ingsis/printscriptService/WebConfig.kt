package edu.ingsis.printscriptService

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    private val logger = LoggerFactory.getLogger(WebConfig::class.java)

    override fun addCorsMappings(registry: CorsRegistry) {
        logger.info("Configuring CORS settings with allowed origins and methods")

        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173", "http://localhost", "https://snippetsearcher.westus2.cloudapp.azure.com", "https://snippetsearcherdev.westus2.cloudapp.azure.com").allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS").allowedHeaders("*")

        logger.info(
            "CORS configured for allowed origins: " +
                "http://localhost:5173, http://localhost, " +
                "https://snippetsearcher.westus2.cloudapp.azure.com, " +
                "https://snippetsearcherdev.westus2.cloudapp.azure.com"
        )
    }
}
