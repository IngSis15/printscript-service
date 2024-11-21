package edu.ingsis.printscriptService.server

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class RequestLogFilter : WebFilter {
    val logger = LoggerFactory.getLogger(RequestLogFilter::class.java)

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val uri = exchange.request.uri
        val method = exchange.request.method.toString()
        val correlationId = MDC.get("correlation-id") ?: "no-correlation-id"

        val prefix = "$method $uri - Correlation ID: $correlationId"

        logger.info("Request received: $prefix")

        try {
            return chain.filter(exchange)
        } finally {
            val statusCode = exchange.response.statusCode
            logger.info("$prefix - Response status: $statusCode")
        }
    }
}

