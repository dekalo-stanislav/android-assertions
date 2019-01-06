package com.heershingenmosiken.assertions.android

import android.os.Handler
import android.os.Looper
import com.heershingenmosiken.assertions.AssertionHandler
import com.heershingenmosiken.assertions.Assertions
import com.heershingenmosiken.assertions.ThrowableFactory

object AndroidAssertions {

    /**
     * Application would crash if assertion happens (if it is not silent assertion).
     */
    fun shouldCrashOnAssertion(crashOnAssertions: Boolean) {
        if (crashOnAssertions) {
            Assertions.addAssertionHandler(AndroidAssertionHandler, Int.MIN_VALUE)
        } else {
            Assertions.removeAssertionHandler(AndroidAssertionHandler)
        }
    }

    /**
     * Add Assertion handler.
     */
    fun addAssertionHandler(assertionHandler: AssertionHandler) {
        Assertions.addAssertionHandler(assertionHandler)
    }

    /**
     * Add Assertion handler.
     */
    fun addAssertionHandler(assertionHandler: (Throwable, Boolean) -> Unit) {
        Assertions.addAssertionHandler(AssertionHandlerWrapper(assertionHandler))
    }

    /**
     * Remove assertion handler.
     */
    fun removeAssertionHandler(assertionHandler: AssertionHandler) {
        Assertions.removeAssertionHandler(assertionHandler)
    }

    /**
     * Remove assertion handler.
     */
    fun removeAssertionHandler(assertionHandler: (Throwable, Boolean) -> Unit) {
        Assertions.removeAssertionHandler(AssertionHandlerWrapper(assertionHandler))
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

data class AssertionHandlerWrapper(val assertionHandler: (Throwable, Boolean) -> Unit) : AssertionHandler {
    override fun report(throwable: Throwable, silently: Boolean) {
        assertionHandler.invoke(throwable, silently)
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