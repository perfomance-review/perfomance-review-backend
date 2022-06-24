package ru.hh.performance_review.security.exception;

public class SecurityServiceException extends SecurityException {

    public SecurityServiceException(String msg) {
        super(msg);
    }

    public SecurityServiceException(String msg, Throwable t) {
        super(msg, t);
    }

}
