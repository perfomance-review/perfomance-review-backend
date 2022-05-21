package ru.hh.performance_review.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Бизнес ошибки сервиса
 */
@Getter
@NoArgsConstructor
public class BusinessServiceException extends RuntimeException {
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

    public BusinessServiceException(int errCode, String errorMessage, String businessMessage) {
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public BusinessServiceException(String message, int errCode, String errorMessage, String businessMessage) {
        super(message);
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public BusinessServiceException(String message, Throwable cause, int errCode, String errorMessage, String businessMessage) {
        super(message, cause);
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public BusinessServiceException(Throwable cause, int errCode, String errorMessage, String businessMessage) {
        super(cause);
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public BusinessServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = errorCode.getErrorDescription();
    }

    public BusinessServiceException(String message, Throwable cause, ErrorCode errorCode, String businessMessage) {
        super(message, cause);
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = businessMessage;
    }

    public BusinessServiceException(ErrorCode errorCode, String businessMessage) {
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = businessMessage;
    }

    public BusinessServiceException(ErrorCode errorCode) {
        this.errCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorDescription();
        this.businessMessage = errorCode.getErrorDescription();
    }

}
