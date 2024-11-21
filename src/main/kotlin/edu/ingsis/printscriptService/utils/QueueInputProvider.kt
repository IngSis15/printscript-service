package edu.ingsis.printscriptService.utils

import lib.InputProvider
import org.slf4j.LoggerFactory
import java.util.Queue

class QueueInputProvider(private val queue: Queue<String>) : InputProvider {
    private val logger = LoggerFactory.getLogger(QueueInputProvider::class.java)

    override fun input(message: String): String {
        val value = queue.poll()
        if (value != null) {
            logger.info("Polled value from queue: $value")
        } else {
            logger.warn("Queue is empty when attempting to poll")
        }
        return value ?: ""
    }
}
