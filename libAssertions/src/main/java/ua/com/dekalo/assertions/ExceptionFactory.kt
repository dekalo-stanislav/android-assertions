package ua.com.dekalo.assertions

interface ExceptionFactory {
    fun create(): Throwable
}