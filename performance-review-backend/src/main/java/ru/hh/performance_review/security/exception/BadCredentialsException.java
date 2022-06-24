package ru.hh.performance_review.security.exception;

public class BadCredentialsException extends SecurityServiceException {
    public BadCredentialsException(String msg) {
        super(msg);
    }

    public BadCredentialsException(String msg, Throwable t) {
        super(msg, t);
    }
}
