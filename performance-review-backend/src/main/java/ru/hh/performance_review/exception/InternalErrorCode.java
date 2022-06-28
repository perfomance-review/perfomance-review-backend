package ru.hh.performance_review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Внутренние ошибки сервиса
 */
@RequiredArgsConstructor
@Getter
public enum InternalErrorCode implements ErrorCode {

    UNAUTHORIZED_TOKEN(6, "Невалидный токен доступа"),
    ACCESS_DENIED(7, "Отсутствуют права доступа"),
    INVALID_SECURED_CONFIGURATION(8, "Ошибка конфигурации безопасности"),

    UNKNOWN_USER(2, "Не найден пользователь: %s"),
    UNKNOWN_POLL(3, "Не найден опрос"),
    SERIALIZATION_ERROR(17, "Ошибка сериализации"),
    DB_SQL_ERROR(92, "Ошибка уровня SQL"),
    VALIDATION_ERROR(95, "Ошибка валидации входных параметров запроса"),
    DB_ACCESS_ERROR(97, "Недоступна БД"),
    REPORT_GENERATE_ERROR(98, "Ошибка генерации %s-документа"),
    INTERNAL_ERROR(99, "Внутренняя ошибка сервиса");


    private final int errorCode;
    private final String errorDescription;

}
