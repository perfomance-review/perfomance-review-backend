package ru.hh.performance_review.security.exception;

import ru.hh.performance_review.exception.ErrorCode;

public class UnauthorizedException extends SecurityServiceException {

    /**
     * Код ошибки
     */
    private int errCode;
    /**
     * Описание ошибки
     */
    private String errorMessage;
    /**
     * Внутреннее описание ошибки
     */
    private String businessMessage;

    public UnauthorizedException(String message, ErrorCode errorCode) {
        super(message);
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = errorCode.getErrorDescription();
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
