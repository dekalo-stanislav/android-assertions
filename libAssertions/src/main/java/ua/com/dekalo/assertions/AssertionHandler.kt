package ua.com.dekalo.assertions

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

class CompositeAssertionHandler(val assertionHandlers: Array<AssertionHandler>) : AssertionHandler {

    override fun report(throwable: Throwable, silently: Boolean) {
        assertionHandlers.forEach { it.report(throwable) }
    }
}