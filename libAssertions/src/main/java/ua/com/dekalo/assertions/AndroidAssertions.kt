package ua.com.dekalo.assertions

import android.os.Handler
import android.os.Looper

object AndroidAssertions {

    fun init(assertionHandler: AssertionHandler = EmptyAssertionHandler) {
        init(assertionHandler, false)
    }

    fun init(crashOnFail: Boolean) {
        init(EmptyAssertionHandler, crashOnFail)
    }

    fun init(assertionHandler: AssertionHandler = EmptyAssertionHandler, crashOnFail: Boolean) {

        Assertions.addAssertionHandler(assertionHandler)

        if (crashOnFail) {
            Assertions.addAssertionHandler(AndroidAssertionHandler, Int.MIN_VALUE)
        }
    }
}

/**
 * Ensures that method called on UI thread, else raises exception provided by ThrowableFactory.
 */
fun Assertions.checkUIThread(throwableFactory: ThrowableFactory) {
    if (Looper.getMainLooper() != Looper.myLooper()) {
        Assertions.fail(throwableFactory)
    }
}

/**
 * Ensures that method called on UI thread, else raises exception provided by ThrowableFactory.
 */
fun Assertions.checkNotUIThread(throwableFactory: ThrowableFactory) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        Assertions.fail(throwableFactory)
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