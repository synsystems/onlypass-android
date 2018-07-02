package org.synsystems.onlypass.framework.io

/**
 * Exception to indicate that data from an external source was incorrectly formatted/structured.
 *
 * @param message a message describing the error
 * @param cause the underlying cause of the error
 */
class MalformedDataException(message: String?, cause: Throwable?) : RuntimeException(message, cause)