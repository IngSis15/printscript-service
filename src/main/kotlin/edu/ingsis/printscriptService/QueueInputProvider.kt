package edu.ingsis.printscriptService

import lib.InputProvider
import java.util.*

class QueueInputProvider(private val queue: Queue<String>) : InputProvider {
    override fun input(message: String): String {
        return queue.poll()
    }
}
