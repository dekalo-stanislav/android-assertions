/*
 * Copyright (c) 2017 Dekalo Stanislav. All rights reserved.
 */
package ua.com.dekalo.assertions;

import android.os.Handler;
import android.os.Looper;

import java.util.Map;

/**
 * Main purpose is to add various assertions in IS_PRODUCTION mode.
 * And switch it off in production.
 * <p/>
 * Created by dekalo on 25.08.15.
 */
public final class Assertions {

    private static final CrashReporter EMPTY_REPORTER = new CrashReporter() {
        @Override
        public void report(Throwable throwable) {
        }
    };

    private static boolean CRASH_ON_FAIL = false;
    private static Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    private static CrashReporter CRASH_REPORTER = EMPTY_REPORTER;

    public interface CrashReporter {
        void report(Throwable throwable);
    }

    private static class ThrowDelegateRunnable implements Runnable {

        private final RuntimeException runtimeException;

        private ThrowDelegateRunnable(Throwable throwable) {
            this.runtimeException = new RuntimeException(throwable);
        }

        @Override
        public void run() {
            throw runtimeException;
        }
    }

    public static void setCrashReporter(CrashReporter crashReporter) {
        CRASH_REPORTER = crashReporter == null ? EMPTY_REPORTER : crashReporter;
    }

    public static boolean isCrashOnFailEnabled() {
        return CRASH_ON_FAIL;
    }

    public static void setCrashOnFail(boolean enabled) {
        CRASH_ON_FAIL = enabled;
    }

    private static void fail(final Throwable e, boolean silently) {
        CRASH_REPORTER.report(e);
        if (CRASH_ON_FAIL && !silently) {
            UI_HANDLER.post(new ThrowDelegateRunnable(e));
        }
    }

    /**
     * Assertion failed with throwable.
     * <p>
     * Same as fail(...) method, but it will not crash application even if  setCrashOnFail(true) was called.
     * <p>
     * This is useful for triggering possible but not desired condition in client code.
     */
    public static void failSilently(final Throwable throwable) {
        fail(throwable, true);
    }

    /**
     * Assertion failed with throwable.
     */
    public static void fail(final Throwable throwable) {
        fail(throwable, false);
    }

    /**
     * Raise exception from ExceptionFactory.
     */
    public static void fail(final ExceptionFactory exceptionFactory) {
        fail(exceptionFactory.create());
    }

    /**
     * Ensures that method called on UI thread, else raises exception provided by ExceptionFactory.
     */
    public static void checkUIThread(ExceptionFactory exceptionFactory) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            fail(exceptionFactory);
        }
    }

    /**
     * Checks that shouldBeTrue condition is true, else raises exception provided by ExceptionFactory.
     */
    public static void assertTrue(boolean shouldBeTrue, ExceptionFactory exceptionFactory) {
        if (!shouldBeTrue) {
            fail(exceptionFactory);
        }
    }

    /**
     * Checks that shouldBeFalse condition is false, else raises exception provided by ExceptionFactory.
     */
    public static void assertFalse(boolean shouldBeFalse, ExceptionFactory exceptionFactory) {
        if (shouldBeFalse) {
            fail(exceptionFactory);
        }
    }

    /**
     * Checks that size of enum equal to size of enum mapping, else raises exception provided by ExceptionFactory.
     */
    public static void assertFullMapping(Class<? extends Enum> enumType, Map enumMapping, ExceptionFactory factory) {
        assertTrue(enumType.getEnumConstants().length == enumMapping.size(), factory);
    }
}
