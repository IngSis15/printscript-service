package edu.ingsis.printscriptService.utils

import lib.PrintEmitter
import org.slf4j.LoggerFactory

class PrintCollector : PrintEmitter {
    private val messages = mutableListOf<String>()
    private val logger = LoggerFactory.getLogger(PrintCollector::class.java)

    override fun print(value: String) {
        logger.info("Received print message: $value")
        messages.add(value)
    }

    fun getMessages(): List<String> {
        logger.debug("Retrieving ${messages.size} messages")
        return messages
    }
}
