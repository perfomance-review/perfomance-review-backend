package ru.hh.performance_review.service.sereliazation;

/**
 * Сервис для сериализации данных
 * используется для {@link ru.hh.performance_review.controller.base.HttpRequestHandler}
 */
public interface ObjectConvertService {

    String convertToJson(Object o);
}
