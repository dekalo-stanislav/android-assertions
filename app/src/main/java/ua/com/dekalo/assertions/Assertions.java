package ua.com.dekalo.assertions;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


/**
 * Main purpose is to add various assertions in IS_PRODUCTION mode.
 * And switch it off in production.
 * <p/>
 * Created by dekalo on 25.08.15.
 */
public class Assertions {

    private static boolean CRASH_ON_FAIL = false;
    private static Handler UI_HANDLER;
    private static AssertionLogger ASSERTION_LOGGER;

    public static void initializeUIHandler() {
        UI_HANDLER = new Handler();
    }

    private static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void setEnabled(boolean enabled) {
        CRASH_ON_FAIL = enabled;
    }

    public static void setAssertionLogger(AssertionLogger assertionLogger) {
        ASSERTION_LOGGER = assertionLogger;
    }

    public static void fail(final Throwable e) {
        if (ASSERTION_LOGGER != null) ASSERTION_LOGGER.log(e);
        if (CRASH_ON_FAIL) performOnUIThreadIfPossible(new ThrowDelegateRunnable(e));
    }

    public static void fail(final Func0<Throwable> descriptionGenerator) {
        fail(descriptionGenerator.call());
    }

    private static void performOnUIThreadIfPossible(Runnable runnable) {
        if (!isUIThread() && UI_HANDLER != null) {
            UI_HANDLER.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void assertNull(Object shouldBeNull, Func0<Throwable> descriptionGenerator) {
        if (shouldBeNull != null) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertNotNull(Object obj, Func0<Throwable> descriptionGenerator) {
        if (obj == null) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertNotEmpty(String string, Func0<Throwable> descriptionGenerator) {
        if (TextUtils.isEmpty(string)) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertEquals(Object desired, Object real, Func0<Throwable> descriptionGenerator) {
        if (!desired.equals(real)) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertEquals(int desired, int real, Func0<Throwable> descriptionGenerator) {
        if (desired != real) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertEquals(long desired, long real, Func0<Throwable> descriptionGenerator) {
        if (desired != real) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertNotEquals(int value, int shouldBeNotEqualTo, Func0<Throwable> descriptionGenerator) {
        if (value == shouldBeNotEqualTo) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertTrue(boolean shouldBeTrue, Func0<Throwable> descriptionGenerator) {
        if (!shouldBeTrue) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertFalse(boolean shouldBeFalse, Func0<Throwable> descriptionGenerator) {
        if (shouldBeFalse) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertUIThread(Func0<Throwable> descriptionGenerator) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            fail(descriptionGenerator.call());
        }
    }

    public static void assertNotUIThread(Func0<Throwable> descriptionGenerator) {
        if (CRASH_ON_FAIL && Looper.myLooper() == Looper.getMainLooper()) {
            fail(descriptionGenerator.call());
        }
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
}