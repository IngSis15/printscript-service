package edu.ingsis.printscriptService.utils

import lib.PrintEmitter

class PrintCollector : PrintEmitter {
    private val messages = mutableListOf<String>()

    override fun print(value: String) {
        messages.add(value)
    }

    fun getMessages(): List<String> {
        return messages
    }
}
