package ru.hh.performance_review.security.exception;

/**
 * Authentication Exception
 */

public class JwtAuthenticationException extends SecurityServiceException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
