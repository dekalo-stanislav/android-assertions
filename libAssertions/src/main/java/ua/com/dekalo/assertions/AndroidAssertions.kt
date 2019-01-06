package ua.com.dekalo.assertions

import android.os.Handler
import android.os.Looper

object AndroidAssertions {
    @JvmOverloads
    fun init(assertionHandler: AssertionHandler = EmptyAssertionHandler, crashOnFail: Boolean = false) {

        Assertions.addAssertionHandler(assertionHandler)

        if (crashOnFail) {
            Assertions.addAssertionHandler(AndroidAssertionHandler, Int.MIN_VALUE)
        }
    }
}

/**
 * Ensures that method called on UI thread, else raises exception provided by ExceptionFactory.
 */
fun Assertions.checkUIThread(exceptionFactory: ExceptionFactory) {
    if (Looper.getMainLooper() != Looper.myLooper()) {
        Assertions.fail(exceptionFactory)
    }
}

/**
 * Ensures that method called on UI thread, else raises exception provided by ExceptionFactory.
 */
fun Assertions.checkNotUIThread(exceptionFactory: ExceptionFactory) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        Assertions.fail(exceptionFactory)
    }
}

object AndroidAssertionHandler : AssertionHandler {

    private val UI_HANDLER = Handler(Looper.getMainLooper())

    override fun report(throwable: Throwable, silently: Boolean) {
        if (!silently) {
            UI_HANDLER.post(ThrowDelegateRunnable(throwable))
        }
    }

}

private class ThrowDelegateRunnable constructor(throwable: Throwable) : Runnable {

    private val runtimeException: RuntimeException = RuntimeException(throwable)

    override fun run() {
        throw runtimeException
    }
}