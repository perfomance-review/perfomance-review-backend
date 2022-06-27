package ru.hh.performance_review.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.hh.performance_review.exception.ErrorCode;

/**
 * Ошибки безопасности
 */
@RequiredArgsConstructor
@Getter
public enum SecuredErrorCode implements ErrorCode {

    UNAUTHORIZED_TOKEN(6, "Невалидный токен доступа"),
    ACCESS_DENIED(7, "Отсутствуют права доступа"),
    INVALID_SECURED_CONFIGURATION(8, "Ошибка конфигурации безопасности: %s"),

    UNKNOWN_USER(2, "Не найден пользователь: %s");

    private final int errorCode;
    private final String errorDescription;

    }
