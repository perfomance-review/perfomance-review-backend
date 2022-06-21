package ru.hh.performance_review.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для проверки прав доступа REST сервиса
 * Устанавливается в параметры метода, помеченных аннотацией
 * @see PerformanceReviewSecured
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface JwtTokenCookie {

    /**
     * @return Строковый имя ролей, для вызова метода
     */
    String jwtToken() default "";

}

