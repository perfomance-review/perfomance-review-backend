package ru.hh.performance_review.exception;

import lombok.Getter;
import ru.hh.performance_review.dto.response.ResponseMessage;

/**
 * Информация об ошибке
 */
@Getter
public class ErrorDto implements ResponseMessage {
    /**
     * Код ошибки
     */
    private final int errCode;
    /**
     * Описание ошибки
     */
    private final String errorMessage;
    /**
     * Внутреннее описание ошибки
     */
    private final String businessMessage;

    public ErrorDto(int errCode, String errorMessage, String businessMessage) {
        this.errCode = errCode;
        this.errorMessage = errorMessage;
        this.businessMessage = businessMessage;
    }

    public ErrorDto(BusinessServiceException e) {
        this.errCode = e.getErrCode();
        this.errorMessage = e.getErrorMessage();
        this.businessMessage = e.getBusinessMessage();
    }

    public ErrorDto(InternalErrorCode internalErrorCode) {
        this.errCode = internalErrorCode.getErrorCode();
        this.errorMessage = internalErrorCode.getErrorDescription();
        this.businessMessage = internalErrorCode.getErrorDescription();
    }

    public ErrorDto(InternalErrorCode internalErrorCode, String businessMessage) {
        this.errCode = internalErrorCode.getErrorCode();
        this.errorMessage = internalErrorCode.getErrorDescription();
        this.businessMessage = businessMessage;
    }

    public ErrorDto(ValidateException e) {
        this.errCode = e.getErrCode();
        this.errorMessage = e.getErrorMessage();
        this.businessMessage = e.getBusinessMessage();
    }
}
