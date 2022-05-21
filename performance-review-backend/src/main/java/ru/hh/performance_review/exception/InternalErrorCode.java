package ru.hh.performance_review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Внутренние ошибки сервиса
 */
@RequiredArgsConstructor
@Getter
public enum InternalErrorCode implements ErrorCode {

    UNKNOWN_USER(2, "Не найден пользователь"),
    SERIALIZATION_ERROR(17, "Ошибка сериализации"),
    DB_SQL_ERROR(92, "Ошибка уровня SQL"),
    VALIDATION_ERROR(95, "Ошибка валидации входных параметров запроса"),
    DB_ACCESS_ERROR(97, "Недоступна БД"),
    INTERNAL_ERROR(99, "Внутренняя ошибка сервиса");


    private final int errorCode;
    private final String errorDescription;

}
