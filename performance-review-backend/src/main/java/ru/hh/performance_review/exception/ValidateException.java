package ru.hh.performance_review.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Ошибка валидации
 */
@Getter
@NoArgsConstructor
public class ValidateException extends RuntimeException {
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

    public ValidateException(int errCode, String errorMessage, String businessMessage) {
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public ValidateException(String message, int errCode, String errorMessage, String businessMessage) {
        super(message);
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public ValidateException(String message, Throwable cause, int errCode, String errorMessage, String businessMessage) {
        super(message, cause);
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public ValidateException(Throwable cause, int errCode, String errorMessage, String businessMessage) {
        super(cause);
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public ValidateException(String message, ErrorCode errorCode) {
        super(message);
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = errorCode.getErrorDescription();
    }

    public ValidateException(Throwable cause, ErrorCode errorCode, String businessMessage) {
        super(businessMessage, cause);
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = businessMessage;
    }

    public ValidateException(ErrorCode errorCode, String businessMessage) {
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = businessMessage;
    }

    public ValidateException(ErrorCode errorCode) {
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = errorCode.getErrorDescription();
    }

}
