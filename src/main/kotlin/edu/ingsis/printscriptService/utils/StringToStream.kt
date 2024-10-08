package edu.ingsis.printscriptService.utils

import java.io.ByteArrayInputStream
import java.io.InputStream

class StringToStream {
    fun convertStringToStream(string: String): InputStream {
        return ByteArrayInputStream(string.toByteArray())
    }
}