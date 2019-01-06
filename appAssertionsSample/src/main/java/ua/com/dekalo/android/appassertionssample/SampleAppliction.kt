package ua.com.dekalo.android.appassertionssample

import android.app.Application
import android.util.Log
import ua.com.dekalo.assertions.AndroidAssertions

class SampleAppliction : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidAssertions.shouldCrashOnAssertion(BuildConfig.DEBUG)
        AndroidAssertions.addAssertionHandler { throwable, silently ->
            // here yoy should send event to Crashlytics or Firebase.
            // this callback will be invoked despite of shouldCrashOnAssertion call.
            Log.e("Assertion", "assertion happnes silently = $silently", throwable)
        }
    }
}
