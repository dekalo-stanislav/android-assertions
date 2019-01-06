package ua.com.dekalo.assertions

class ChainedException : Throwable {

    constructor(message: String, throwable: Throwable) : super(message, throwable) {}

    constructor(throwable: Throwable) : super(throwable) {}
}
