/*
 * Copyright (c) 2017 Dekalo Stanislav. All rights reserved.
 */
package com.heershingenmosiken.assertions

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

    private val handlers = TreeSet<PriorityHandler>()

    /**
     * Add AssertionHandler to handler list.
     */
    fun addAssertionHandler(assertionHandler: AssertionHandler) {
        handlers.add(PriorityHandler(assertionHandler))
    }

    /**
     * Add AssertionHandler to handler list, with DEFAULT_PRIORITY = 0.
     */
    fun addAssertionHandler(assertionHandler: AssertionHandler, priority: Int = DEFAULT_PRIOTIY) {
        handlers.add(PriorityHandler(assertionHandler, priority))
    }

    /**
     * Remove AssertionHandler from handlers list.
     */
    fun removeAssertionHandler(assertionHandler: AssertionHandler) {
        Utils.filter(handlers) { it.handler == assertionHandler }
    }

    private fun hasHandlers(): Boolean = !handlers.isEmpty()

    /**
     * Assertion failed with throwable.
     */
    fun fail(throwable: Throwable) {
        if (hasHandlers()) {
            handlers.forEach { it.handler.report(throwable, false) }
        }
    }

    /**
     * Assertion failed with throwable.
     *
     * @param silently will be passed to AssertionHandler, crash will not happen in any case if it is silent assertion. It is reasonable if it is possible but undesired situation that you would like to log in your crash reporting tool.
     */
    fun failSilently(throwable: Throwable) {
        if (hasHandlers()) {
            handlers.forEach { it.handler.report(throwable, true) }
        }
    }

    /**
     * Assertion failed with throwable factory.
     */
    fun fail(throwableFactory: ThrowableFactory) {
        if (hasHandlers()) {
            val throwable = throwableFactory.create();
            handlers.forEach { it.handler.report(throwable, false) }
        }
    }

    /**
     * Checks that shouldBeTrue condition is true, else raises exception provided by ThrowableFactory.
     */
    fun assertTrue(shouldBeTrue: Boolean, throwableFactory: ThrowableFactory) {
        if (!shouldBeTrue) {
            fail(throwableFactory)
        }
    }

    /**
     * Checks that shouldBeFalse condition is false, else raises exception provided by ThrowableFactory.
     */
    fun assertFalse(shouldBeFalse: Boolean, throwableFactory: ThrowableFactory) {
        if (shouldBeFalse) {
            fail(throwableFactory)
        }
    }

    fun assertNull(shouldBeNull: Any?, throwableFactory: ThrowableFactory) {
        if (shouldBeNull != null) {
            fail(throwableFactory)
        }
    }

    fun assertNotNull(shouldNotBeNull: Any?, throwableFactory: ThrowableFactory) {
        if (shouldNotBeNull == null) {
            fail(throwableFactory)
        }
    }

    fun <T> assertEmpty(shouldBeEmpty: Collection<T>, throwableFactory: ThrowableFactory) {
        if (!shouldBeEmpty.isEmpty()) {
            fail(throwableFactory)
        }
    }

    fun <T> assertNotEmpty(shouldNotBeEmpty: Collection<T>, throwableFactory: ThrowableFactory) {
        if (shouldNotBeEmpty.isEmpty()) {
            fail(throwableFactory)
        }
    }

    private data class PriorityHandler(val handler: AssertionHandler, val priority: Int = DEFAULT_PRIOTIY) : Comparable<PriorityHandler> {
        override fun compareTo(other: PriorityHandler) = other.priority - priority
    }
}
