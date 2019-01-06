/*
 * Copyright (c) 2017 Dekalo Stanislav. All rights reserved.
 */
package ua.com.dekalo.assertions

import java.util.*


/**
 * Main purpose is to add various assertions in IS_PRODUCTION mode.
 * And switch it off in production.
 *
 * Assertion not works until you will add AssertionHandler.
 *
 * Created by dekalo on 25.08.15.
 */
object Assertions {

    private const val DEFAULT_PRIOTIY = 0;

    private val handlers = TreeSet<Pair<Int, AssertionHandler>>()

    /**
     * Add AssertionHandler to handler list.
     */
    @JvmOverloads
    fun addAssertionHandler(assertionHandler: AssertionHandler, priority: Int = DEFAULT_PRIOTIY) {
        handlers.add(Pair(priority, assertionHandler))
    }

    /**
     * Remove AssertionHandler from handlers list.
     */
    fun removeAssertionHandler(assertionHandler: AssertionHandler) {
        while (handlers.remove(handlers.find { it.second == assertionHandler })) {
            // all logic already in while statement
        }
    }

    private fun hasHandlers(): Boolean = handlers.isNotEmpty()

    /**
     * Assertion failed with throwable.
     *
     * @param silently will be passed to AssertionHandler, crash will not happen in any case if it is silent assertion. It is reasonable if it is possible but undesired situation that you would like to log in your crash reporting tool.
     */
    @JvmOverloads
    fun fail(throwable: Throwable, silently: Boolean = false) {
        if (hasHandlers()) {
            handlers.forEach { it.second.report(throwable, silently) }
        }
    }

    internal fun fail(exceptionFactory: ExceptionFactory) {
        if (hasHandlers()) {
            exceptionFactory.create().let { throwable ->
                handlers.forEach { it.second.report(throwable, false) }
            }
        }
    }

    /**
     * Checks that shouldBeTrue condition is true, else raises exception provided by ExceptionFactory.
     */
    fun assertTrue(shouldBeTrue: Boolean, exceptionFactory: ExceptionFactory) {
        if (!shouldBeTrue) {
            fail(exceptionFactory)
        }
    }

    /**
     * Checks that shouldBeFalse condition is false, else raises exception provided by ExceptionFactory.
     */
    fun assertFalse(shouldBeFalse: Boolean, exceptionFactory: ExceptionFactory) {
        if (shouldBeFalse) {
            fail(exceptionFactory)
        }
    }

    fun assertNull(shouldBeNull: Any?, exceptionFactory: ExceptionFactory) {
        if (shouldBeNull != null) {
            fail(exceptionFactory)
        }
    }

    fun assertNotNull(shouldNotBeNull: Any?, exceptionFactory: ExceptionFactory) {
        if (shouldNotBeNull == null) {
            fail(exceptionFactory)
        }
    }

    fun <T> assertEmpty(shouldBeEmpty: Collection<T>, exceptionFactory: ExceptionFactory) {
        if (shouldBeEmpty.isNotEmpty()) {
            fail(exceptionFactory)
        }
    }

    fun <T> assertNotEmpty(shouldNotBeEmpty: Collection<T>, exceptionFactory: ExceptionFactory) {
        if (shouldNotBeEmpty.isEmpty()) {
            fail(exceptionFactory)
        }
    }
}
