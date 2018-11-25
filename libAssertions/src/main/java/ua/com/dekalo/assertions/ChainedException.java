package ua.com.dekalo.assertions;

public class ChainedException extends Throwable {

    public ChainedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ChainedException(Throwable throwable) {
        super(throwable);
    }
}
