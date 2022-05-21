package ru.hh.performance_review.exception;

/**
 * Маркер для кодов ошибок
 */
public interface ErrorCode {

    int getErrorCode();

    String getErrorDescription();
}
