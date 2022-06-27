package ru.hh.performance_review.security.exception;


/**
 * InvalidSecuredConfiguration Exception
 */

public class InvalidSecuredConfigurationException extends SecurityServiceException {
    public InvalidSecuredConfigurationException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidSecuredConfigurationException(String msg) {
        super(msg);
    }

    public InvalidSecuredConfigurationException(SecuredErrorCode securedErrorCode, Throwable t) {
        super(securedErrorCode.getErrorDescription(), t);
    }

    public InvalidSecuredConfigurationException(SecuredErrorCode securedErrorCode) {
        super(securedErrorCode.getErrorDescription());
    }
}
