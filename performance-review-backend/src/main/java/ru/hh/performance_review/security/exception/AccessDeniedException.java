package ru.hh.performance_review.security.exception;

public class AccessDeniedException extends SecurityServiceException {
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
