package com.heershingenmosiken.assertions

/**
 * Handler for happened assertion.
 */
interface AssertionHandler {
    /**
     * Report happened assertion.
     *
     * @param throwable - information about assertion (message + stack trace).
     * @param silently - if assertion should be handled silently.
     */
    fun report(throwable: Throwable, silently: Boolean = false)
}

object EmptyAssertionHandler : AssertionHandler {
    override fun report(throwable: Throwable, silently: Boolean) {
        // do nothing
    }
}